<?php
require_once __DIR__ . '/../db_helper.php';

require_http_method('POST');
$session = require_api_role('alumno');

$alum_codigo = isset($_POST['alum_codigo']) ? trim((string) $_POST['alum_codigo']) : $session['api_user_code'];
if ($alum_codigo === '' || $alum_codigo !== $session['api_user_code']) {
    send_json_error('No autorizado para comprar por otro alumno', 403);
}

$conn = get_db_connection();
$mensaje = call_procedure_message($conn, 'registrar_compra_almuerzo', 's', array($session['api_user_code']));
$conn->close();

$ok = ($mensaje === 'Compra exitosa');
send_json(
    array(
        'ok' => $ok,
        'mensaje' => $mensaje !== null ? $mensaje : 'No se pudo procesar la compra',
    ),
    $ok ? 200 : 409
);
