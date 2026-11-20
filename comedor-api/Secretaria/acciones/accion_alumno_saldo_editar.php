<?php
require_once __DIR__ . '/../../db_helper.php';

require_http_method('POST');
require_api_role('secretaria');

$alum_codigo = require_param('POST', 'alum_codigo');
$saldo = require_int_param('POST', 'saldo');

if ($saldo < 0) {
    send_json_error('El saldo no puede ser negativo', 400);
}

$conn = get_db_connection();
$alumnos = fetch_all_prepared(
    $conn,
    'SELECT alum_id, alum_beca FROM alumnos WHERE alum_codigo = ? LIMIT 1',
    's',
    array($alum_codigo)
);
if (empty($alumnos)) {
    $conn->close();
    send_json_error('Alumno no encontrado', 404);
}
if ($alumnos[0]['alum_beca'] === 'si') {
    $conn->close();
    send_json_error('No se puede editar el saldo de un alumno becado', 400);
}

$affected = execute_prepared(
    $conn,
    'UPDATE alumnos SET alum_saldo = ? WHERE alum_id = ?',
    'ii',
    array($saldo, (int) $alumnos[0]['alum_id'])
);
$conn->close();

send_json(array('ok' => true, 'mensaje' => 'Saldo actualizado correctamente', 'affected' => $affected));
