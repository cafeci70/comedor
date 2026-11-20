<?php
require_once __DIR__ . '/db_helper.php';

require_http_method('POST');
api_logout_session();

send_json(array('ok' => true, 'mensaje' => 'Sesión cerrada correctamente'));
