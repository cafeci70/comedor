package com.ejemplo.comedor.fragmento_alumno;

import com.ejemplo.comedor.R;

/**
 * Fragment displaying the purchased daily dinner ticket and QR code.
 */
public class fragmento_alumno_reporte_cena extends BaseReporteTicketFragment {

    public fragmento_alumno_reporte_cena() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_fragmento_alumno_reporte_cena;
    }

    @Override
    protected String getReporteEndpointPath() {
        return "/comedor-api/Alumno/reportes/reporte_ticket_cena.php";
    }

    @Override
    protected String getEntradaField() {
        return "menu_cen_entrada";
    }

    @Override
    protected String getPostreField() {
        return "menu_cen_postre";
    }

    @Override
    protected String getPlatoPrincipalField() {
        return "menu_cena";
    }

    @Override
    protected String getBebidaField() {
        return "menu_cen_bebida";
    }

    @Override
    protected String getTipoComidaLabel() {
        return "Cena";
    }
}
