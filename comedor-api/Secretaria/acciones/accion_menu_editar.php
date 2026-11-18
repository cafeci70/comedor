<?php
require_once __DIR__ . '/../../db_helper.php';

$menu_id          = require_int_param('POST', 'id_menu');
$fecha            = require_date_param('POST', 'fecha');
$desayuno         = require_param('POST', 'desayuno');
$almuerzo_entrada = require_param('POST', 'almuerzo_entrada');
$almuerzo_postre  = require_param('POST', 'almuerzo_postre');
$almuerzo         = require_param('POST', 'almuerzo');
$almuerzo_bebida  = require_param('POST', 'almuerzo_bebida');
$cena_entrada     = require_param('POST', 'cena_entrada');
$cena_postre      = require_param('POST', 'cena_postre');
$cena             = require_param('POST', 'cena');
$cena_bebida      = require_param('POST', 'cena_bebida');

$conn = get_db_connection();
$affected = execute_prepared(
    $conn,
    'UPDATE menu
     SET menu_fecha = ?, menu_desayuno = ?, menu_alm_entrada = ?, menu_alm_postre = ?,
         menu_almuerzo = ?, menu_alm_bebida = ?, menu_cen_entrada = ?, menu_cen_postre = ?,
         menu_cena = ?, menu_cen_bebida = ?
     WHERE menu_id = ?',
    'ssssssssssi',
    array(
        $fecha, $desayuno, $almuerzo_entrada, $almuerzo_postre, $almuerzo,
        $almuerzo_bebida, $cena_entrada, $cena_postre, $cena, $cena_bebida, $menu_id
    )
);
$conn->close();

send_json(array('ok' => true, 'affected' => $affected));
