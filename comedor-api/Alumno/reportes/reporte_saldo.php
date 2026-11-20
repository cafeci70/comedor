<?php
require_once __DIR__ . '/../../db_helper.php';

$session = require_api_role('alumno');
$id_alumno = require_int_param('GET', 'id_alumno');
if ($id_alumno !== (int) $session['api_user_id']) {
    send_json_error('No autorizado para consultar el saldo de otro alumno', 403);
}

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT alum_saldo FROM alumnos WHERE alum_id = ?',
    'i',
    array($id_alumno)
);
$conn->close();

send_json($datos);
