<?php
require_once __DIR__ . '/../../db_helper.php';

$alum_id = require_int_param('GET', 'alum_id');

$conn = get_db_connection();
$compras = fetch_all_prepared(
    $conn,
    'SELECT comp_id, comp_qr, comp_fecha, comp_almuerzo, comp_cena, alum_id, menu_id
     FROM compra
     WHERE alum_id = ? AND comp_fecha = CURDATE()',
    'i',
    array($alum_id)
);
$conn->close();

send_json($compras);
