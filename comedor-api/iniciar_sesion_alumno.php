<?php
require_once __DIR__ . '/db_helper.php';

require_http_method('POST');

$codigo = require_param('POST', 'codigo');
$clave = require_param('POST', 'clave');

if (!rate_limit_check('login_alumno', $codigo)) {
    send_json_error('Demasiados intentos fallidos. Espere unos minutos.', 429);
}

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT alum_id, alum_nombres, alum_apellido_pa, alum_apellido_ma, alum_correo,
            alum_celular, alum_saldo, alum_beca, alum_codigo, alum_pass
     FROM alumnos
     WHERE alum_codigo = ?
     LIMIT 1',
    's',
    array($codigo)
);
$conn->close();

if (empty($datos) || !verify_stored_password($clave, $datos[0]['alum_pass'])) {
    rate_limit_record_failure('login_alumno', $codigo);
    send_json_error('Código o clave incorrectos', 401);
}

rate_limit_clear('login_alumno', $codigo);
$alumno = $datos[0];
unset($alumno['alum_pass']);

api_login_session('alumno', $alumno['alum_id'], $alumno['alum_codigo']);
send_json(array($alumno));
