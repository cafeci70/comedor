<?php
require_once __DIR__ . '/auth.php';
reportes_require_auth();

require_once __DIR__ . '/reporte_helper.php';

$conn = get_db_connection();
$fecha = resolver_fecha_reporte($conn);
$resumen = obtener_resumen_ventas($conn, $fecha);
$conn->close();

$alm = $resumen['almuerzo'];
$cen = $resumen['cena'];

$tituloAlmuerzo = 'Almuerzo: ' . $alm['nombre']
    . '<br>Total Tickets Vendidos: ' . $alm['total']
    . '<br>Becados: ' . $alm['becados']
    . '<br>No Becados: ' . $alm['nobecados'];

$tituloCena = 'Cena: ' . $cen['nombre']
    . '<br>Total Tickets Vendidos: ' . $cen['total']
    . '<br>Becados: ' . $cen['becados']
    . '<br>No Becados: ' . $cen['nobecados'];
?>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="Highcharts-7.1.2/code/css/app.css">
    <title>REPORTES</title>
</head>
<body>
<p align="right"><a href="logout.php">Cerrar sesión</a></p>
<script src="Highcharts-7.1.2/code/highcharts.js"></script>
<script src="Highcharts-7.1.2/code/modules/highcharts-3d.js"></script>

<h3 align="center">COMPRA DEL <?php echo htmlspecialchars($resumen['fecha'], ENT_QUOTES, 'UTF-8'); ?></h3>

<?php renderizar_grafico_torta('container', $tituloAlmuerzo, $alm['becados'], $alm['nobecados']); ?>
<?php renderizar_grafico_torta('cena', $tituloCena, $cen['becados'], $cen['nobecados']); ?>

</body>
</html>
