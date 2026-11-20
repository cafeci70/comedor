<?php
/**
 * Autenticación de los reportes web (panel HTML con Highcharts).
 *
 * Las credenciales son las mismas de la secretaría (tabla `usuario`).
 * Se usa una sesión de PHP con cookie restringida a esta carpeta, httponly
 * y SameSite=Lax; las contraseñas guardadas como hash se verifican con
 * password_verify() y las antiguas en texto plano con comparación segura.
 */

require_once __DIR__ . '/../db_helper.php';

$reportes_session_inactivity = 1800; // 30 minutos

if (session_status() === PHP_SESSION_NONE) {
    $scriptDir = str_replace('\\', '/', dirname(isset($_SERVER['SCRIPT_NAME']) ? $_SERVER['SCRIPT_NAME'] : '/'));
    $cookiePath = rtrim($scriptDir, '/') . '/';

    $isHttps = is_https_request();

    session_name('COMEDORREPORTES');
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
    ini_set('session.gc_maxlifetime', (string) $reportes_session_inactivity);
    session_start();
}

/**
 * Verifica código y clave contra la tabla de usuarios.
 *
 * @param mysqli $conn
 * @param string $codigo
 * @param string $clave
 * @return array|null Fila del usuario o null si las credenciales no coinciden.
 */
function reportes_verificar_credenciales($conn, $codigo, $clave)
{
    $rows = fetch_all_prepared(
        $conn,
        'SELECT usu_id, usu_nombre, usu_codigo_dni, usu_contrasena
         FROM usuario WHERE usu_codigo_dni = ? LIMIT 1',
        's',
        array($codigo)
    );
    if (empty($rows)) {
        return null;
    }

    $row = $rows[0];
    return verify_stored_password($clave, $row['usu_contrasena']) ? $row : null;
}

/**
 * Marca la sesión actual como autenticada para el usuario dado.
 *
 * @param array $usuario
 */
function reportes_iniciar_sesion($usuario)
{
    session_regenerate_id(true);
    $_SESSION['comedor_auth'] = true;
    $_SESSION['comedor_usuario'] = (string) $usuario['usu_codigo_dni'];
    $_SESSION['comedor_nombre'] = (string) $usuario['usu_nombre'];
    $_SESSION['comedor_last_activity'] = time();
    unset($_SESSION['comedor_csrf']);
}

/**
 * Datos de la cookie de sesión, usados por la app Android para el WebView.
 *
 * @return array
 */
function reportes_session_cookie_data()
{
    $params = session_get_cookie_params();
    $cookiePath = isset($params['path']) ? $params['path'] : '/';
    return array(
        'name' => session_name(),
        'value' => session_id(),
        'path' => $cookiePath,
        'cookie_name' => session_name(),
        'cookie_value' => session_id(),
        'cookie_path' => $cookiePath,
    );
}

/**
 * Genera (o devuelve) el token anti-CSRF del formulario de acceso.
 *
 * @return string
 */
function reportes_csrf_token()
{
    if (empty($_SESSION['comedor_csrf'])) {
        if (function_exists('random_bytes')) {
            $_SESSION['comedor_csrf'] = bin2hex(random_bytes(32));
        } else {
            $_SESSION['comedor_csrf'] = md5(uniqid('comedor', true));
        }
    }
    return (string) $_SESSION['comedor_csrf'];
}

/**
 * Muestra el formulario de acceso y termina la petición.
 *
 * @param string $error
 */
function reportes_mostrar_login($error = '')
{
    $token = reportes_csrf_token();
    $errorEscapado = $error !== '' ? htmlspecialchars($error, ENT_QUOTES, 'UTF-8') : '';
    ?>
    <!DOCTYPE HTML>
    <html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Acceso a Reportes del Comedor</title>
        <style type="text/css">
            body { font-family: Arial, sans-serif; margin: 24px; color: #1d3b36; }
            .login { max-width: 360px; padding: 20px; border: 1px solid #cfe0dc; border-radius: 6px; }
            label { display: block; margin-top: 12px; font-weight: bold; }
            input[type=text], input[type=password] { width: 100%; padding: 8px; box-sizing: border-box; }
            button { margin-top: 18px; padding: 9px 16px; background: #00574b; color: #fff; border: 0; cursor: pointer; }
            .error { color: #b00020; margin-top: 12px; }
        </style>
    </head>
    <body>
        <div class="login">
            <h2>Reportes del Comedor</h2>
            <form method="post" action="">
                <input type="hidden" name="csrf" value="<?php echo htmlspecialchars($token, ENT_QUOTES, 'UTF-8'); ?>">
                <label for="codigo">Código / DNI</label>
                <input type="text" id="codigo" name="codigo" required autocomplete="username">
                <label for="clave">Clave</label>
                <input type="password" id="clave" name="clave" required autocomplete="current-password">
                <button type="submit">Ingresar</button>
                <?php if ($errorEscapado !== '') { ?>
                    <p class="error"><?php echo $errorEscapado; ?></p>
                <?php } ?>
            </form>
        </div>
    </body>
    </html>
    <?php
    exit;
}

/**
 * Procesa el envío del formulario y, si no hay sesión válida, muestra el login.
 */
function reportes_require_auth()
{
    global $reportes_session_inactivity;

    $ultima = isset($_SESSION['comedor_last_activity']) ? (int) $_SESSION['comedor_last_activity'] : 0;
    if (!empty($_SESSION['comedor_auth']) && time() - $ultima < $reportes_session_inactivity) {
        $_SESSION['comedor_last_activity'] = time();
        return;
    }

    if ($_SERVER['REQUEST_METHOD'] === 'POST'
        && isset($_POST['codigo'], $_POST['clave'], $_POST['csrf'])) {
        $token = isset($_SESSION['comedor_csrf']) ? (string) $_SESSION['comedor_csrf'] : '';
        $enviado = (string) $_POST['csrf'];

        if ($token === '' || !hash_equals($token, $enviado)) {
            reportes_mostrar_login('Sesión inválida. Intente nuevamente.');
        }

        $codigo = trim((string) $_POST['codigo']);
        $clave = (string) $_POST['clave'];
        if ($codigo === '' || $clave === '') {
            reportes_mostrar_login('Ingrese código y clave.');
        }

        if (!rate_limit_check('reportes_login', $codigo)) {
            reportes_mostrar_login('Demasiados intentos fallidos. Espere unos minutos.');
        }

        $conn = get_db_connection();
        $usuario = reportes_verificar_credenciales($conn, $codigo, $clave);
        $conn->close();

        if ($usuario === null) {
            rate_limit_record_failure('reportes_login', $codigo);
            reportes_mostrar_login('Código o clave incorrectos.');
        }

        rate_limit_clear('reportes_login', $codigo);
        reportes_iniciar_sesion($usuario);
        header('Location: index.php');
        exit;
    }

    if (!empty($_SESSION['comedor_auth'])) {
        // Sesión expirada: se limpia para evitar reutilización.
        $_SESSION = array();
    }

    reportes_mostrar_login();
}

/**
 * Renders a CSRF-protected POST logout control.
 */
function reportes_render_logout_link()
{
    $token = htmlspecialchars(reportes_csrf_token(), ENT_QUOTES, 'UTF-8');
    echo '<form method="post" action="logout.php" style="text-align:right;margin:0 0 12px 0;">'
        . '<input type="hidden" name="csrf" value="' . $token . '">'
        . '<button type="submit" style="background:none;border:0;color:#00574b;font-weight:bold;cursor:pointer;padding:0;font-size:inherit;">Cerrar sesión</button>'
        . '</form>';
}
