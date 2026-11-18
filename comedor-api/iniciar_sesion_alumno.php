<?php
require_once __DIR__ . '/db_helper.php';

$codigo = require_param('GET', 'codigo');
$clave = require_param('GET', 'clave');

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT alum_id, alum_nombres, alum_apellido_pa, alum_apellido_ma, alum_correo, alum_celular, alum_saldo, alum_beca, alum_codigo
     FROM alumnos
     WHERE alum_codigo = ? AND alum_pass = ?',
    'ss',
    array($codigo, $clave)
);
$conn->close();

send_json($datos);
