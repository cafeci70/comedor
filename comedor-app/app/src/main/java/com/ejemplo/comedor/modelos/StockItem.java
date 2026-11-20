package com.ejemplo.comedor.modelos;

/**
 * Represents a scholarship stock period row returned by reporte_stock.php.
 */
public class StockItem {
    public final String stocId;
    public final String periodo;
    public final String becados;

    public StockItem(String stocId, String periodo, String becados) {
        this.stocId = stocId;
        this.periodo = periodo;
        this.becados = becados;
    }
}
