<?php
require_once __DIR__ . '/db_helper.php';

$codigo = require_param('GET', 'codigo');
$clave = require_param('GET', 'clave');

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT usu_id, usu_nombre, usu_apellido, usu_direccion, usu_celular, usu_codigo_dni, usu_correo
     FROM usuario
     WHERE usu_codigo_dni = ? AND usu_contrasena = ?',
    'ss',
    array($codigo, $clave)
);
$conn->close();

send_json($datos);
