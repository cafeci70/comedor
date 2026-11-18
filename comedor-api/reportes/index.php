<?php
require_once __DIR__ . '/auth.php';
reportes_require_auth();

$hoy = date('Y-m-d');
$fechaParam = isset($_GET['fecha']) ? trim((string) $_GET['fecha']) : $hoy;
$fechaEscapada = htmlspecialchars($fechaParam, ENT_QUOTES, 'UTF-8');
?>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reportes del Comedor</title>
    <style type="text/css">
        body { font-family: Arial, sans-serif; margin: 24px; color: #1d3b36; }
        ul { line-height: 2; }
        a { color: #00574b; text-decoration: none; font-weight: bold; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <p align="right"><a href="logout.php">Cerrar sesión</a></p>
    <h2>Reportes de Venta de Tickets</h2>
    <ul>
        <li><a href="reporte_almuerzo_cena.php?fecha=<?php echo urlencode($fechaParam); ?>">Resumen diario (Almuerzo y Cena - <?php echo $fechaEscapada; ?>)</a></li>
        <li><a href="reporte_almuerzo.php?fecha=<?php echo urlencode($fechaParam); ?>">Reporte de Almuerzo (<?php echo $fechaEscapada; ?>)</a></li>
        <li><a href="reporte_cena.php?fecha=<?php echo urlencode($fechaParam); ?>">Reporte de Cena (<?php echo $fechaEscapada; ?>)</a></li>
    </ul>
</body>
</html>
