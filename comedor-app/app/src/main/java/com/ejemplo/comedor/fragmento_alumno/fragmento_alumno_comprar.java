package com.ejemplo.comedor.fragmento_alumno;

import androidx.fragment.app.Fragment;

import com.ejemplo.comedor.R;

/**
 * Fragment for purchasing the daily lunch ticket.
 */
public class fragmento_alumno_comprar extends BaseCompraTicketFragment {

    public fragmento_alumno_comprar() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_fragmento_alumno_comprar;
    }

    @Override
    protected int getBuyButtonId() {
        return R.id.btn_comprar_almuerzo;
    }

    @Override
    protected int getStockTextViewId() {
        return R.id.stock_ticket_almuerzo;
    }

    @Override
    protected String getCompraCheckField() {
        return "comp_almuerzo";
    }

    @Override
    protected String getStockBecadoField() {
        return "menu_alm_contbeca";
    }

    @Override
    protected String getStockRegularField() {
        return "menu_alm_cont";
    }

    @Override
    protected String getRegistrarCompraPath() {
        return "/comedor-api/Alumno/registrar_compra_almuerzo.php";
    }

    @Override
    protected Fragment createReportFragment() {
        return new fragmento_alumno_reporte_almuerzo();
    }
}
