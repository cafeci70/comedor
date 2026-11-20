<?php
require_once __DIR__ . '/../../db_helper.php';

require_api_role('secretaria');

$alum_codigo = require_param('GET', 'alum_codigo');

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT alum_nombres FROM alumnos WHERE alum_codigo = ?',
    's',
    array($alum_codigo)
);
$conn->close();

send_json($datos);
