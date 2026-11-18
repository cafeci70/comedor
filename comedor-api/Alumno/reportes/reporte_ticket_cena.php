<?php
require_once __DIR__ . '/../../db_helper.php';

$id_alumno = require_int_param('GET', 'id_alumno');

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    "SELECT c.comp_id, c.comp_fecha, a.alum_nombres,
            m.menu_cen_entrada, m.menu_cen_postre, m.menu_cena, m.menu_cen_bebida
     FROM compra c
     INNER JOIN alumnos a ON a.alum_id = c.alum_id
     INNER JOIN menu m ON m.menu_id = c.menu_id
     WHERE c.comp_fecha = CURDATE()
       AND a.alum_id = ?
       AND c.comp_cena = 'si'",
    'i',
    array($id_alumno)
);
$conn->close();

send_json($datos);
