<?php
require_once __DIR__ . '/../../db_helper.php';

$stock_periodo = require_param('POST', 'periodo');
$stock_becados = require_int_param('POST', 'becados');

if ($stock_becados < 0) {
    send_json_error('La cantidad de becados no puede ser negativa', 400);
}

$conn = get_db_connection();
$affected = execute_prepared(
    $conn,
    'INSERT INTO stock (stoc_periodo, stoc_becados) VALUES (?, ?)',
    'si',
    array($stock_periodo, $stock_becados)
);
$conn->close();

send_json(array('ok' => true, 'affected' => $affected));
