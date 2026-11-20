<?php
require_once __DIR__ . '/../../db_helper.php';

$session = require_api_role('alumno');
$id_alumno = require_int_param('GET', 'id_alumno');
if ($id_alumno !== (int) $session['api_user_id']) {
    send_json_error('No autorizado para consultar el ticket de otro alumno', 403);
}

$conn = get_db_connection();
$datos = fetch_all_prepared(
    $conn,
    "SELECT c.comp_id, c.comp_qr, c.comp_fecha, a.alum_nombres,
            m.menu_alm_entrada, m.menu_alm_postre, m.menu_almuerzo, m.menu_alm_bebida
     FROM compra c
     INNER JOIN alumnos a ON a.alum_id = c.alum_id
     INNER JOIN menu m ON m.menu_id = c.menu_id
     WHERE c.comp_fecha = CURDATE()
       AND a.alum_id = ?
       AND c.comp_almuerzo = 'si'",
    'i',
    array($id_alumno)
);
$conn->close();

send_json($datos);
