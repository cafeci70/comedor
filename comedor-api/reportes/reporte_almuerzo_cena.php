<?php
require_once __DIR__ . '/auth.php';
reportes_require_auth();

require_once __DIR__ . '/reporte_helper.php';
renderizar_pagina_resumen_diario();
