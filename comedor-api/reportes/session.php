<?php
/**
 * Endpoint usado por la app Android para abrir la sesión de los reportes
 * antes de cargar el WebView. Devuelve los datos de la cookie de sesión.
 */

require_once __DIR__ . '/auth.php';

require_http_method('POST');

$codigo = require_param('POST', 'codigo');
$clave = require_param('POST', 'clave');

if (!rate_limit_check('reportes_login', $codigo)) {
    send_json_error('Demasiados intentos fallidos. Espere unos minutos.', 429);
}

$conn = get_db_connection();
$usuario = reportes_verificar_credenciales($conn, $codigo, $clave);
$conn->close();

if ($usuario === null) {
    rate_limit_record_failure('reportes_login', $codigo);
    send_json_error('Credenciales inválidas', 401);
}

rate_limit_clear('reportes_login', $codigo);
reportes_iniciar_sesion($usuario);
send_json(array('ok' => true) + reportes_session_cookie_data());
