<?php
require_once __DIR__ . '/../../db_helper.php';

$alum_codigo = require_param('POST', 'alum_codigo');
$saldo       = require_int_param('POST', 'saldo');

if ($saldo <= 0) {
    send_json_error('El monto de recarga debe ser mayor a cero', 400);
}

$conn = get_db_connection();
$mensaje = call_procedure_message($conn, 'generar_recarga', 'si', array($alum_codigo, $saldo));
$conn->close();

$ok = ($mensaje !== null && stripos($mensaje, 'recarg') !== false);
send_json(array(
    'ok' => $ok,
    'mensaje' => $mensaje !== null ? $mensaje : 'Operación procesada',
));
