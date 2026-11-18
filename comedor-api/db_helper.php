<?php
require_once __DIR__ . '/conexion.php';

/**
 * Shared helpers for JSON responses, input validation, and prepared SQL execution.
 */

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
