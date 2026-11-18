<?php
require_once __DIR__ . '/../../db_helper.php';

$id_alumno = require_int_param('GET', 'id_alumno');

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT alum_saldo FROM alumnos WHERE alum_id = ?',
    'i',
    array($id_alumno)
);
$conn->close();

send_json($datos);
