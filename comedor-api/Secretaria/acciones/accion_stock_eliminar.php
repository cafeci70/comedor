<?php
require_once __DIR__ . '/../../db_helper.php';

require_http_method('POST');
require_api_role('secretaria');

$stock_id = require_int_param('POST', 'id_stock');

$conn = get_db_connection();
$affected = execute_prepared(
    $conn,
    'DELETE FROM stock WHERE stoc_id = ?',
    'i',
    array($stock_id)
);
$conn->close();

send_json(array('ok' => true, 'affected' => $affected));
