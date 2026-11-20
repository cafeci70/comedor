<?php
require_once __DIR__ . '/../../db_helper.php';

require_api_role(array('alumno', 'secretaria'));

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    'SELECT menu_alm_cont, menu_alm_contbeca, menu_cen_cont, menu_cen_contbeca
     FROM menu
     WHERE menu_fecha = CURDATE()'
);
$conn->close();

send_json($datos);
