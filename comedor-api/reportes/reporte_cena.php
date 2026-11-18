<?php
require_once __DIR__ . '/auth.php';
reportes_require_auth();

require_once __DIR__ . '/reporte_helper.php';

$conn = get_db_connection();
$fecha = resolver_fecha_reporte($conn);
$resumen = obtener_resumen_ventas($conn, $fecha);
$conn->close();

$cen = $resumen['cena'];
$tituloCena = 'Compra del ' . $resumen['fecha'] . ' - Cena: ' . $cen['nombre']
    . '<br>TOTAL DE TICKETS VENDIDOS: ' . $cen['total']
    . '<br>Becados: ' . $cen['becados']
    . '<br>No Becados: ' . $cen['nobecados'];
?>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="Highcharts-7.1.2/code/css/app.css">
    <title>REPORTES - CENA</title>
</head>
<body>
<p align="right"><a href="logout.php">Cerrar sesión</a></p>
<script src="Highcharts-7.1.2/code/highcharts.js"></script>
<script src="Highcharts-7.1.2/code/modules/exporting.js"></script>
<script src="Highcharts-7.1.2/code/modules/export-data.js"></script>
<script src="Highcharts-7.1.2/code/modules/highcharts-3d.js"></script>

<?php renderizar_grafico_torta('cena', $tituloCena, $cen['becados'], $cen['nobecados']); ?>

</body>
</html>
