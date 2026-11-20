<?php
require_once __DIR__ . '/conexion.php';

/**
 * Shared helpers for JSON responses, input validation, authentication,
 * rate limiting, and prepared SQL execution.
 */

$appTimezone = getenv('APP_TIMEZONE');
if ($appTimezone === false || trim($appTimezone) === '') {
    $appTimezone = 'America/Lima';
}
@date_default_timezone_set($appTimezone);

/**
 * Returns true if the current request arrived over HTTPS (directly or via proxy).
 *
 * @return bool
 */
function is_https_request()
{
    return (!empty($_SERVER['HTTPS']) && strtolower((string) $_SERVER['HTTPS']) !== 'off')
        || (isset($_SERVER['HTTP_X_FORWARDED_PROTO'])
            && strtolower((string) $_SERVER['HTTP_X_FORWARDED_PROTO']) === 'https');
}

/**
 * Resolves the URL base path of the comedor-api directory for session cookies.
 *
 * @return string
 */
function api_cookie_path()
{
    $scriptName = str_replace('\\', '/', isset($_SERVER['SCRIPT_NAME']) ? $_SERVER['SCRIPT_NAME'] : '/');
    $apiRoot = realpath(__DIR__);
    $scriptFile = isset($_SERVER['SCRIPT_FILENAME']) ? realpath($_SERVER['SCRIPT_FILENAME']) : false;

    if ($apiRoot !== false && $scriptFile !== false) {
        $apiRootNorm = rtrim(str_replace('\\', '/', $apiRoot), '/');
        $scriptDirNorm = rtrim(str_replace('\\', '/', dirname($scriptFile)), '/');
        if (strpos($scriptDirNorm, $apiRootNorm) === 0) {
            $rel = str_replace('\\', '/', substr($scriptDirNorm, strlen($apiRootNorm)));
            $urlDir = rtrim(str_replace('\\', '/', dirname($scriptName)), '/');
            if ($rel !== '' && substr($urlDir, -strlen($rel)) === $rel) {
                $base = substr($urlDir, 0, strlen($urlDir) - strlen($rel));
                return ($base === '' ? '' : $base) . '/';
            }
            return ($urlDir === '' ? '' : $urlDir) . '/';
        }
    }

    $pos = strpos($scriptName, '/comedor-api/');
    if ($pos !== false) {
        return substr($scriptName, 0, $pos + strlen('/comedor-api/'));
    }
    return '/';
}

/**
 * Starts the API PHP session scoped to the comedor-api root path.
 */
function api_start_session()
{
    if (session_status() === PHP_SESSION_NONE) {
        $inactivity = 1800;
        $cookiePath = api_cookie_path();
        $isHttps = is_https_request();

        session_name('COMEDORAPI');
        if (PHP_VERSION_ID >= 70300) {
            session_set_cookie_params(array(
                'lifetime' => 0,
                'path' => $cookiePath,
                'domain' => '',
                'secure' => $isHttps,
                'httponly' => true,
                'samesite' => 'Lax',
            ));
        } else {
            session_set_cookie_params(0, $cookiePath, '', $isHttps, true);
            ini_set('session.cookie_samesite', 'Lax');
        }
        ini_set('session.use_strict_mode', '1');
        ini_set('session.gc_maxlifetime', (string) $inactivity);
        session_start();
    }
}

/**
 * Stores authenticated user state in the API session and regenerates the ID.
 *
 * @param string $role 'alumno' or 'secretaria'
 * @param int $userId
 * @param string $userCode
 */
function api_login_session($role, $userId, $userCode)
{
    api_start_session();
    session_regenerate_id(true);
    $_SESSION['api_auth'] = true;
    $_SESSION['api_role'] = (string) $role;
    $_SESSION['api_user_id'] = (int) $userId;
    $_SESSION['api_user_code'] = (string) $userCode;
    $_SESSION['api_last_activity'] = time();
}

/**
 * Destroys the current API session and expires its cookie.
 */
function api_logout_session()
{
    api_start_session();
    $_SESSION = array();

    if (ini_get('session.use_cookies')) {
        $params = session_get_cookie_params();
        setcookie(
            session_name(),
            '',
            time() - 42000,
            $params['path'],
            $params['domain'],
            $params['secure'],
            $params['httponly']
        );
    }

    session_destroy();
}

/**
 * Ensures the request has an active API session with one of the allowed roles.
 *
 * @param string|array $allowedRoles
 * @return array Session payload
 */
function require_api_role($allowedRoles)
{
    api_start_session();

    $inactivity = 1800;
    $lastActivity = isset($_SESSION['api_last_activity']) ? (int) $_SESSION['api_last_activity'] : 0;
    if (empty($_SESSION['api_auth']) || (time() - $lastActivity) >= $inactivity) {
        api_logout_session();
        send_json_error('Sesión no iniciada o expirada', 401);
    }

    $_SESSION['api_last_activity'] = time();

    $roles = is_array($allowedRoles) ? $allowedRoles : array($allowedRoles);
    $currentRole = isset($_SESSION['api_role']) ? (string) $_SESSION['api_role'] : '';
    if (!in_array($currentRole, $roles, true)) {
        send_json_error('No tiene permisos para realizar esta operación', 403);
    }

    return $_SESSION;
}

/**
 * Verifies a plaintext password against a password_hash() digest or legacy plaintext.
 *
 * @param string $plain
 * @param string $stored
 * @return bool
 */
function verify_stored_password($plain, $stored)
{
    $plainStr = (string) $plain;
    $storedStr = (string) $stored;
    if ($plainStr === '' || $storedStr === '') {
        return false;
    }
    $info = password_get_info($storedStr);
    if (!empty($info['algo'])) {
        return password_verify($plainStr, $storedStr);
    }
    return hash_equals($storedStr, $plainStr);
}

/**
 * Returns the client identifier used for rate limiting.
 *
 * @return string
 */
function rate_limit_client_ip()
{
    return isset($_SERVER['REMOTE_ADDR']) ? (string) $_SERVER['REMOTE_ADDR'] : 'unknown';
}

/**
 * Builds the storage path for a rate limit bucket.
 *
 * @param string $scope
 * @param string $identifier
 * @return string
 */
function rate_limit_file_path($scope, $identifier)
{
    $key = hash('sha256', $scope . '|' . rate_limit_client_ip() . '|' . $identifier);
    return rtrim(sys_get_temp_dir(), '/\\') . DIRECTORY_SEPARATOR . 'comedor_rl_' . $key . '.json';
}

/**
 * Checks whether an identifier is still within the allowed failed login threshold.
 *
 * @param string $scope
 * @param string $identifier
 * @param int $maxAttempts
 * @param int $windowSeconds
 * @return bool
 */
function rate_limit_check($scope, $identifier, $maxAttempts = 10, $windowSeconds = 300)
{
    $path = rate_limit_file_path($scope, $identifier);
    if (!is_file($path)) {
        return true;
    }
    $raw = @file_get_contents($path);
    if ($raw === false) {
        return true;
    }
    $data = json_decode($raw, true);
    if (!is_array($data) || !isset($data['attempts'], $data['first_at'])) {
        return true;
    }
    if ((time() - (int) $data['first_at']) >= $windowSeconds) {
        @unlink($path);
        return true;
    }
    return ((int) $data['attempts']) < $maxAttempts;
}

/**
 * Records a failed authentication attempt in the rate limit bucket.
 *
 * @param string $scope
 * @param string $identifier
 * @param int $windowSeconds
 */
function rate_limit_record_failure($scope, $identifier, $windowSeconds = 300)
{
    $path = rate_limit_file_path($scope, $identifier);
    $fp = @fopen($path, 'c+');
    if (!$fp) {
        return;
    }
    if (@flock($fp, LOCK_EX)) {
        $raw = stream_get_contents($fp);
        $data = is_string($raw) && $raw !== '' ? json_decode($raw, true) : null;
        $now = time();

        if (!is_array($data) || !isset($data['attempts'], $data['first_at']) || ($now - (int) $data['first_at']) >= $windowSeconds) {
            $data = array('attempts' => 1, 'first_at' => $now);
        } else {
            $data['attempts'] = (int) $data['attempts'] + 1;
        }

        ftruncate($fp, 0);
        rewind($fp);
        fwrite($fp, json_encode($data));
        fflush($fp);
        flock($fp, LOCK_UN);
    }
    fclose($fp);
}

/**
 * Clears any recorded failed login attempts after a successful authentication.
 *
 * @param string $scope
 * @param string $identifier
 */
function rate_limit_clear($scope, $identifier)
{
    $path = rate_limit_file_path($scope, $identifier);
    if (is_file($path)) {
        @unlink($path);
    }
}

/**
 * Enforces an expected HTTP request method (e.g. POST).
 *
 * @param string $method
 */
function require_http_method($method)
{
    $actual = isset($_SERVER['REQUEST_METHOD']) ? strtoupper((string) $_SERVER['REQUEST_METHOD']) : '';
    if ($actual !== strtoupper($method)) {
        send_json_error('Método HTTP no permitido', 405);
    }
}

/**
 * Opens a validated mysqli connection or terminates with a 500 JSON response.
 *
 * @return mysqli
 */
function get_db_connection()
{
    $mysql = new conexion();
    $conn = $mysql->get_conexion();
    if (!$conn || $conn->connect_errno) {
        send_json_error('Error de conexión con la base de datos', 500);
    }
    return $conn;
}

/**
 * Emits a UTF-8 JSON response and sets the HTTP status code.
 *
 * @param mixed $data
 * @param int $statusCode
 */
function send_json($data, $statusCode = 200)
{
    if (!headers_sent()) {
        http_response_code($statusCode);
        header('Content-Type: application/json; charset=utf-8');
    }
    $json = json_encode($data, JSON_UNESCAPED_UNICODE);
    if ($json === false) {
        if (!headers_sent()) {
            http_response_code(500);
        }
        echo json_encode(array('ok' => false, 'mensaje' => 'Error al codificar JSON'));
        return;
    }
    echo $json;
}

/**
 * Sends a JSON error payload and halts execution.
 *
 * @param string $message
 * @param int $statusCode
 */
function send_json_error($message, $statusCode = 400)
{
    send_json(array('ok' => false, 'mensaje' => $message), $statusCode);
    exit;
}

/**
 * Reads and validates a non-empty parameter from GET or POST.
 *
 * @param string $method 'GET' or 'POST'
 * @param string $key
 * @return string
 */
function require_param($method, $key)
{
    $source = strtoupper($method) === 'POST' ? $_POST : $_GET;
    if (!isset($source[$key])) {
        send_json_error("Falta el parámetro obligatorio: $key", 400);
    }
    $value = trim((string) $source[$key]);
    if ($value === '') {
        send_json_error("El parámetro '$key' no puede estar vacío", 400);
    }
    return $value;
}

/**
 * Reads and validates an integer parameter from GET or POST.
 *
 * @param string $method 'GET' or 'POST'
 * @param string $key
 * @return int
 */
function require_int_param($method, $key)
{
    $raw = require_param($method, $key);
    $val = filter_var($raw, FILTER_VALIDATE_INT);
    if ($val === false) {
        send_json_error("El parámetro '$key' debe ser un número entero válido", 400);
    }
    return (int) $val;
}

/**
 * Reads and validates a YYYY-MM-DD date parameter from GET or POST.
 *
 * @param string $method 'GET' or 'POST'
 * @param string $key
 * @return string
 */
function require_date_param($method, $key)
{
    $raw = require_param($method, $key);
    if (!preg_match('/^(\d{4})-(\d{1,2})-(\d{1,2})$/', $raw, $m)) {
        send_json_error("El parámetro '$key' debe tener formato YYYY-MM-DD", 400);
    }
    if (!checkdate((int) $m[2], (int) $m[3], (int) $m[1])) {
        send_json_error("La fecha en '$key' no es válida", 400);
    }
    return sprintf('%04d-%02d-%02d', (int) $m[1], (int) $m[2], (int) $m[3]);
}

/**
 * Fetches all rows as associative arrays from an executed mysqli_stmt,
 * supporting both mysqlnd (get_result) and legacy mysqli drivers.
 *
 * @param mysqli_stmt $stmt
 * @return array
 */
function stmt_fetch_all_assoc($stmt)
{
    $rows = array();
    if (method_exists($stmt, 'get_result')) {
        $result = $stmt->get_result();
        if ($result instanceof mysqli_result) {
            while ($row = $result->fetch_assoc()) {
                $rows[] = $row;
            }
            $result->free();
        }
        return $rows;
    }

    $meta = $stmt->result_metadata();
    if (!$meta) {
        return $rows;
    }
    $fields = array();
    $rowRefs = array();
    while ($field = $meta->fetch_field()) {
        $fields[$field->name] = null;
        $rowRefs[] = &$fields[$field->name];
    }
    $meta->free();

    call_user_func_array(array($stmt, 'bind_result'), $rowRefs);
    while ($stmt->fetch()) {
        $copy = array();
        foreach ($fields as $k => $v) {
            $copy[$k] = $v;
        }
        $rows[] = $copy;
    }
    return $rows;
}

/**
 * Prepares, binds parameters (keeping bound variables in scope until execute finishes),
 * and executes a SQL statement. Terminates with HTTP 500 JSON on failure.
 *
 * @param mysqli $conn
 * @param string $sql
 * @param string $types
 * @param array $params
 * @return mysqli_stmt
 */
function prepare_and_execute($conn, $sql, $types = '', $params = array())
{
    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        error_log('MySQL prepare error (' . $conn->errno . '): ' . $conn->error . ' | SQL: ' . $sql);
        send_json_error('Error interno al preparar la operación SQL', 500);
    }
    if ($types !== '' && !empty($params)) {
        $refs = array($types);
        foreach ($params as $idx => $value) {
            $refs[] = &$params[$idx];
        }
        call_user_func_array(array($stmt, 'bind_param'), $refs);
    }
    if (!$stmt->execute()) {
        error_log('MySQL execute error (' . $stmt->errno . '): ' . $stmt->error);
        $stmt->close();
        send_json_error('Error interno al ejecutar la operación SQL', 500);
    }
    return $stmt;
}

/**
 * Executes a prepared SELECT query and returns all rows as an associative array.
 *
 * @param mysqli $conn
 * @param string $sql
 * @param string $types
 * @param array $params
 * @return array
 */
function fetch_all_prepared($conn, $sql, $types = '', $params = array())
{
    $stmt = prepare_and_execute($conn, $sql, $types, $params);
    $rows = stmt_fetch_all_assoc($stmt);
    $stmt->close();
    return $rows;
}

/**
 * Executes a prepared INSERT/UPDATE/DELETE statement and returns affected row count.
 *
 * @param mysqli $conn
 * @param string $sql
 * @param string $types
 * @param array $params
 * @return int
 */
function execute_prepared($conn, $sql, $types = '', $params = array())
{
    $stmt = prepare_and_execute($conn, $sql, $types, $params);
    $affected = $stmt->affected_rows;
    $stmt->close();
    return $affected;
}

/**
 * Calls a stored procedure using placeholders, drains any trailing result sets,
 * and returns the first row's 'mensaje' column if provided.
 *
 * @param mysqli $conn
 * @param string $procName Alphanumeric procedure name
 * @param string $types
 * @param array $params
 * @return string|null
 */
function call_procedure_message($conn, $procName, $types = '', $params = array())
{
    $placeholders = empty($params) ? '' : implode(', ', array_fill(0, count($params), '?'));
    $sql = "CALL `$procName`($placeholders)";
    $stmt = prepare_and_execute($conn, $sql, $types, $params);

    $rows = stmt_fetch_all_assoc($stmt);
    $stmt->close();

    // Drain any extra result sets produced by CALL so the connection remains reusable.
    while ($conn->more_results() && $conn->next_result()) {
        $extra = $conn->store_result();
        if ($extra instanceof mysqli_result) {
            $extra->free();
        }
    }

    if (!empty($rows) && isset($rows[0]['mensaje'])) {
        return (string) $rows[0]['mensaje'];
    }
    return null;
}
