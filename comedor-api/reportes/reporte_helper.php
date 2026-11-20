<?php
require_once __DIR__ . '/../db_helper.php';

/**
 * Resolves a validated YYYY-MM-DD date from $_GET['fecha'] or defaults to MySQL CURDATE().
 *
 * @param mysqli $conn
 * @return string
 */
function resolver_fecha_reporte($conn)
{
    if (isset($_GET['fecha']) && trim((string) $_GET['fecha']) !== '') {
        $raw = trim((string) $_GET['fecha']);
        if (preg_match('/^(\d{4})-(\d{1,2})-(\d{1,2})$/', $raw, $m)
            && checkdate((int) $m[2], (int) $m[3], (int) $m[1])) {
            return sprintf('%04d-%02d-%02d', (int) $m[1], (int) $m[2], (int) $m[3]);
        }
    }
    $rows = fetch_all_prepared($conn, 'SELECT CURDATE() AS hoy');
    return !empty($rows) ? (string) $rows[0]['hoy'] : date('Y-m-d');
}

/**
 * Fetches menu names and meal purchase counts (scholarship vs regular) for a date.
 *
 * @param mysqli $conn
 * @param string $fecha
 * @return array
 */
function obtener_resumen_ventas($conn, $fecha)
{
    $menus = fetch_all_prepared(
        $conn,
        'SELECT menu_fecha, menu_almuerzo, menu_cena FROM menu WHERE menu_fecha = ? LIMIT 1',
        's',
        array($fecha)
    );
    $nombreAlmuerzo = !empty($menus) && $menus[0]['menu_almuerzo'] !== null
        ? (string) $menus[0]['menu_almuerzo']
        : 'Sin menú registrado';
    $nombreCena = !empty($menus) && $menus[0]['menu_cena'] !== null
        ? (string) $menus[0]['menu_cena']
        : 'Sin menú registrado';

    $totales = fetch_all_prepared(
        $conn,
        "SELECT
            COALESCE(SUM(CASE WHEN c.comp_almuerzo = 'si' AND a.alum_beca = 'si' THEN 1 ELSE 0 END), 0) AS alm_becados,
            COALESCE(SUM(CASE WHEN c.comp_almuerzo = 'si' AND a.alum_beca = 'no' THEN 1 ELSE 0 END), 0) AS alm_nobecados,
            COALESCE(SUM(CASE WHEN c.comp_cena = 'si' AND a.alum_beca = 'si' THEN 1 ELSE 0 END), 0) AS cen_becados,
            COALESCE(SUM(CASE WHEN c.comp_cena = 'si' AND a.alum_beca = 'no' THEN 1 ELSE 0 END), 0) AS cen_nobecados
         FROM compra c
         INNER JOIN alumnos a ON a.alum_id = c.alum_id
         WHERE c.comp_fecha = ?",
        's',
        array($fecha)
    );

    $row = !empty($totales) ? $totales[0] : array();
    $almB = isset($row['alm_becados']) ? (int) $row['alm_becados'] : 0;
    $almNB = isset($row['alm_nobecados']) ? (int) $row['alm_nobecados'] : 0;
    $cenB = isset($row['cen_becados']) ? (int) $row['cen_becados'] : 0;
    $cenNB = isset($row['cen_nobecados']) ? (int) $row['cen_nobecados'] : 0;

    return array(
        'fecha' => $fecha,
        'almuerzo' => array(
            'nombre' => $nombreAlmuerzo,
            'becados' => $almB,
            'nobecados' => $almNB,
            'total' => $almB + $almNB,
        ),
        'cena' => array(
            'nombre' => $nombreCena,
            'becados' => $cenB,
            'nobecados' => $cenNB,
            'total' => $cenB + $cenNB,
        ),
    );
}

/**
 * Renders a Highcharts pie chart block safely escaping title strings via json_encode.
 *
 * @param string $containerId
 * @param string $titulo
 * @param int $becados
 * @param int $noBecados
 */
function renderizar_grafico_torta($containerId, $titulo, $becados, $noBecados)
{
    $jsonFlags = JSON_UNESCAPED_UNICODE | JSON_HEX_TAG | JSON_HEX_AMP | JSON_HEX_APOS | JSON_HEX_QUOT;
    $containerJs = json_encode($containerId, $jsonFlags);
    $tituloJs = json_encode($titulo, $jsonFlags);
    $becadosInt = (int) $becados;
    $noBecadosInt = (int) $noBecados;
    ?>
<div class="chart-card" id="<?php echo htmlspecialchars($containerId, ENT_QUOTES, 'UTF-8'); ?>"></div>
<script type="text/javascript">
Highcharts.chart(<?php echo $containerJs; ?>, {
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
    },
    accessibility: {
        enabled: false
    },
    credits: {
        enabled: false
    },
    title: {
        text: <?php echo $tituloJs; ?>
    },
    tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b> ({point.y})'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                style: {
                    color: '#1d3b36'
                }
            }
        }
    },
    series: [{
        name: 'Tickets',
        colorByPoint: true,
        data: [
            { name: 'Becados', y: <?php echo $becadosInt; ?> },
            { name: 'No becados', y: <?php echo $noBecadosInt; ?> }
        ]
    }]
});
</script>
<?php
}

/**
 * Renders the unified responsive daily sales report page (lunch and dinner).
 */
function renderizar_pagina_resumen_diario()
{
    $conn = get_db_connection();
    $fecha = resolver_fecha_reporte($conn);
    $resumen = obtener_resumen_ventas($conn, $fecha);
    $conn->close();

    $alm = $resumen['almuerzo'];
    $cen = $resumen['cena'];
    $fechaEsc = htmlspecialchars($resumen['fecha'], ENT_QUOTES, 'UTF-8');
    $nombreAlmEsc = htmlspecialchars($alm['nombre'], ENT_QUOTES, 'UTF-8');
    $nombreCenEsc = htmlspecialchars($cen['nombre'], ENT_QUOTES, 'UTF-8');

    $tituloAlmuerzo = 'Almuerzo: ' . $nombreAlmEsc
        . '<br>Total Tickets Vendidos: ' . $alm['total']
        . '<br>Becados: ' . $alm['becados']
        . '<br>No Becados: ' . $alm['nobecados'];

    $tituloCena = 'Cena: ' . $nombreCenEsc
        . '<br>Total Tickets Vendidos: ' . $cen['total']
        . '<br>Becados: ' . $cen['becados']
        . '<br>No Becados: ' . $cen['nobecados'];
    ?>
<!DOCTYPE HTML>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportes del Comedor</title>
    <style type="text/css">
        * { box-sizing: border-box; }
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 16px;
            background: #f4f8f7;
            color: #1d3b36;
        }
        .topbar {
            max-width: 1100px;
            margin: 0 auto 12px auto;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: center;
            gap: 10px;
        }
        .fecha-form {
            display: flex;
            align-items: center;
            gap: 8px;
            flex-wrap: wrap;
        }
        .fecha-form input[type="date"] {
            padding: 6px 8px;
            border: 1px solid #b8d1cc;
            border-radius: 4px;
            color: #1d3b36;
            background: #fff;
        }
        .fecha-form button {
            padding: 7px 12px;
            background: #00574b;
            color: #fff;
            border: 0;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
        }
        .titulo-principal {
            text-align: center;
            margin: 8px 0 18px 0;
            color: #00574b;
        }
        .charts-grid {
            max-width: 1100px;
            margin: 0 auto;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 16px;
        }
        .chart-card {
            background: #fff;
            border: 1px solid #d7e6e3;
            border-radius: 8px;
            padding: 10px;
            min-height: 320px;
            width: 100%;
        }
    </style>
</head>
<body>
    <div class="topbar">
        <form class="fecha-form" method="get" action="">
            <label for="fecha"><strong>Fecha:</strong></label>
            <input type="date" id="fecha" name="fecha" value="<?php echo $fechaEsc; ?>">
            <button type="submit">Ver</button>
        </form>
        <?php reportes_render_logout_link(); ?>
    </div>

    <script src="js/highcharts.js"></script>

    <h3 class="titulo-principal">COMPRA DEL <?php echo $fechaEsc; ?></h3>

    <div class="charts-grid">
        <?php renderizar_grafico_torta('almuerzo', $tituloAlmuerzo, $alm['becados'], $alm['nobecados']); ?>
        <?php renderizar_grafico_torta('cena', $tituloCena, $cen['becados'], $cen['nobecados']); ?>
    </div>
</body>
</html>
<?php
}


