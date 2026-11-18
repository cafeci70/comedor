<?php
require_once __DIR__ . '/../db_helper.php';

$alum_codigo = require_param('POST', 'alum_codigo');

$conn = get_db_connection();
$mensaje = call_procedure_message($conn, 'registrar_compra_almuerzo', 's', array($alum_codigo));
$conn->close();

$ok = ($mensaje === 'Compra exitosa');
send_json(array(
    'ok' => $ok,
    'mensaje' => $mensaje !== null ? $mensaje : 'No se pudo procesar la compra',
));
