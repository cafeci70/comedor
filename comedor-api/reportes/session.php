<?php
/**
 * Endpoint usado por la app Android para abrir la sesión de los reportes
 * antes de cargar el WebView. Devuelve los datos de la cookie de sesión.
 */

require_once __DIR__ . '/auth.php';

$codigo = require_param('POST', 'codigo');
$clave = require_param('POST', 'clave');

$conn = get_db_connection();
$usuario = reportes_verificar_credenciales($conn, $codigo, $clave);
$conn->close();

if ($usuario === null) {
    send_json_error('Credenciales inválidas', 401);
}

reportes_iniciar_sesion($usuario);
send_json(array('ok' => true) + reportes_session_cookie_data());
