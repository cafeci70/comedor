<?php
require_once __DIR__ . '/../../db_helper.php';

$conn = get_db_connection();
$stock = fetch_all_prepared(
    $conn,
    'SELECT stoc_id, stoc_periodo, stoc_becados
     FROM stock
     ORDER BY stoc_id DESC'
);
$conn->close();

send_json($stock);
