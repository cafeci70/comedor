<?php
require_once __DIR__ . '/../../db_helper.php';

require_api_role(array('alumno', 'secretaria'));

$conn = get_db_connection();
$horario = fetch_all_prepared(
    $conn,
    'SELECT menu_id, menu_fecha, menu_desayuno, menu_alm_entrada, menu_alm_postre,
            menu_almuerzo, menu_alm_bebida, menu_cen_entrada, menu_cen_postre,
            menu_cena, menu_cen_bebida
     FROM menu
     ORDER BY menu_fecha DESC'
);
$conn->close();

send_json($horario);
