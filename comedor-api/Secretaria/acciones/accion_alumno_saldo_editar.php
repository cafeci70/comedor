<?php
require_once __DIR__ . '/../../db_helper.php';

$alum_codigo = require_param('POST', 'alum_codigo');
$saldo = require_int_param('POST', 'saldo');

$conn = get_db_connection();
$affected = execute_prepared(
    $conn,
    "UPDATE alumnos SET alum_saldo = ? WHERE alum_codigo = ? AND alum_beca = 'no'",
    'is',
    array($saldo, $alum_codigo)
);
$conn->close();

send_json(array('ok' => true, 'affected' => $affected));
