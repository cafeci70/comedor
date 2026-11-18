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
    $containerJs = json_encode($containerId);
    $tituloJs = json_encode($titulo, JSON_UNESCAPED_UNICODE);
    $becadosInt = (int) $becados;
    $noBecadosInt = (int) $noBecados;
    ?>
<div id="<?php echo htmlspecialchars($containerId, ENT_QUOTES, 'UTF-8'); ?>"></div>
<script type="text/javascript">
Highcharts.chart(<?php echo $containerJs; ?>, {
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
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
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
            }
        }
    },
    series: [{
        name: 'Lista de MENU',
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

