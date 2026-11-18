package com.ejemplo.comedor.fragmento_alumno;

import androidx.fragment.app.Fragment;

import com.ejemplo.comedor.R;

/**
 * Fragment for purchasing the daily dinner ticket.
 */
public class fragmento_alumno_comprar_cena extends BaseCompraTicketFragment {

    public fragmento_alumno_comprar_cena() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_fragmento_alumno_comprar_cena;
    }

    @Override
    protected int getBuyButtonId() {
        return R.id.btn_comprar_cena;
    }

    @Override
    protected int getStockTextViewId() {
        return R.id.stock_ticket_cena;
    }

    @Override
    protected String getCompraCheckField() {
        return "comp_cena";
    }

    @Override
    protected String getStockBecadoField() {
        return "menu_cen_contbeca";
    }

    @Override
    protected String getStockRegularField() {
        return "menu_cen_cont";
    }

    @Override
    protected String getRegistrarCompraPath() {
        return "/comedor-api/Alumno/registrar_compra_cena.php";
    }

    @Override
    protected Fragment createReportFragment() {
        return new fragmento_alumno_reporte_cena();
    }
}
