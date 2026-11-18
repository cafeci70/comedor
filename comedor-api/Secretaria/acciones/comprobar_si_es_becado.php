<?php
require_once __DIR__ . '/../../db_helper.php';

$alum_codigo = require_param('GET', 'alum_codigo');

$conn = get_db_connection();
$alumno = fetch_all_prepared(
    $conn,
    'SELECT alum_id, alum_nombres, alum_codigo, alum_saldo, alum_beca
     FROM alumnos
     WHERE alum_codigo = ?',
    's',
    array($alum_codigo)
);
$conn->close();

send_json($alumno);
