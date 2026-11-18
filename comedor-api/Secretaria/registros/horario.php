<?php
require_once __DIR__ . '/../../db_helper.php';

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
call_procedure_message(
    $conn,
    'registrar_menu',
    'ssssssssss',
    array(
        $fecha, $desayuno, $almuerzo_entrada, $almuerzo_postre, $almuerzo,
        $almuerzo_bebida, $cena_entrada, $cena_postre, $cena, $cena_bebida
    )
);
$conn->close();

send_json(array('ok' => true, 'mensaje' => 'Menú registrado correctamente'));
