<?php
require_once __DIR__ . '/../../db_helper.php';

require_api_role('secretaria');

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    "SELECT COUNT(*) AS total
     FROM alumnos a
     INNER JOIN compra c ON a.alum_id = c.alum_id
     INNER JOIN menu m ON m.menu_id = c.menu_id
     WHERE c.comp_fecha = CURDATE() AND c.comp_cena = 'si'"
);
$conn->close();

send_json($datos);
