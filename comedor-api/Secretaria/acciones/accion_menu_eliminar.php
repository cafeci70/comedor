<?php
require_once __DIR__ . '/../../db_helper.php';

$menu_id = require_int_param('POST', 'id_menu');

$conn = get_db_connection();
$affected = execute_prepared(
    $conn,
    'DELETE FROM menu WHERE menu_id = ?',
    'i',
    array($menu_id)
);
$conn->close();

send_json(array('ok' => true, 'affected' => $affected));
