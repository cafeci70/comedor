<?php
require_once __DIR__ . '/db_helper.php';

require_http_method('POST');

$codigo = require_param('POST', 'codigo');
$clave = require_param('POST', 'clave');

if (!rate_limit_check('login_secretaria', $codigo)) {
    send_json_error('Demasiados intentos fallidos. Espere unos minutos.', 429);
}

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT usu_id, usu_nombre, usu_apellido, usu_direccion, usu_celular,
            usu_codigo_dni, usu_correo, usu_contrasena
     FROM usuario
     WHERE usu_codigo_dni = ?
     LIMIT 1',
    's',
    array($codigo)
);
$conn->close();

if (empty($datos) || !verify_stored_password($clave, $datos[0]['usu_contrasena'])) {
    rate_limit_record_failure('login_secretaria', $codigo);
    send_json_error('Código o clave incorrectos', 401);
}

rate_limit_clear('login_secretaria', $codigo);
$usuario = $datos[0];
unset($usuario['usu_contrasena']);

api_login_session('secretaria', $usuario['usu_id'], $usuario['usu_codigo_dni']);
send_json(array($usuario));
