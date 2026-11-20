<?php
require_once __DIR__ . '/../../db_helper.php';

require_http_method('POST');
require_api_role('secretaria');

$menu_id = require_int_param('POST', 'id_menu');

$conn = get_db_connection();
$compras = fetch_all_prepared(
    $conn,
    'SELECT COUNT(*) AS total FROM compra WHERE menu_id = ?',
    'i',
    array($menu_id)
);
if (!empty($compras) && (int) $compras[0]['total'] > 0) {
    $conn->close();
    send_json_error('No se puede eliminar el menú porque ya tiene compras registradas', 409);
}

$affected = execute_prepared(
    $conn,
    'DELETE FROM menu WHERE menu_id = ?',
    'i',
    array($menu_id)
);
$conn->close();

send_json(array('ok' => true, 'mensaje' => 'Menú eliminado correctamente', 'affected' => $affected));
