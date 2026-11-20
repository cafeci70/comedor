<?php
require_once __DIR__ . '/../../db_helper.php';

require_http_method('POST');
require_api_role('secretaria');

$stock_id      = require_int_param('POST', 'id_stock');
$stock_periodo = require_param('POST', 'periodo');
$stock_becados = require_int_param('POST', 'becados');

if ($stock_becados < 0) {
    send_json_error('La cantidad de becados no puede ser negativa', 400);
}

$conn = get_db_connection();
$affected = execute_prepared(
    $conn,
    'UPDATE stock SET stoc_periodo = ?, stoc_becados = ? WHERE stoc_id = ?',
    'sii',
    array($stock_periodo, $stock_becados, $stock_id)
);
$conn->close();

send_json(array('ok' => true, 'affected' => $affected));
