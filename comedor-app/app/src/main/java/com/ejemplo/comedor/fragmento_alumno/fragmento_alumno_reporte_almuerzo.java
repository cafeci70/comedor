package com.ejemplo.comedor.fragmento_alumno;

import com.ejemplo.comedor.R;

/**
 * Fragment displaying the purchased daily lunch ticket and QR code.
 */
public class fragmento_alumno_reporte_almuerzo extends BaseReporteTicketFragment {

    public fragmento_alumno_reporte_almuerzo() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_fragmento_alumno_reporte_almuerzo;
    }

    @Override
    protected String getReporteEndpointPath() {
        return "/comedor-api/Alumno/reportes/reporte_ticket_almuerzo.php";
    }

    @Override
    protected String getEntradaField() {
        return "menu_alm_entrada";
    }

    @Override
    protected String getPostreField() {
        return "menu_alm_postre";
    }

    @Override
    protected String getPlatoPrincipalField() {
        return "menu_almuerzo";
    }

    @Override
    protected String getBebidaField() {
        return "menu_alm_bebida";
    }

    @Override
    protected String getTipoComidaLabel() {
        return "Almuerzo";
    }
}
