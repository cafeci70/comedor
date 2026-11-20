package com.ejemplo.comedor.modelos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ejemplo.comedor.R;

import java.util.List;

/**
 * Unified row adapter for the scholarship stock table.
 */
public class StockAdapter extends BaseAdapter {

    public interface OnStockActionListener {
        void onEdit(StockItem item);

        void onDelete(StockItem item);
    }

    private final LayoutInflater inflater;
    private final List<StockItem> items;
    private final OnStockActionListener listener;

    public StockAdapter(Context context, List<StockItem> items, OnStockActionListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public StockItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_stock_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StockItem item = getItem(position);
        holder.tvPeriodo.setText(item.periodo);
        holder.tvBecados.setText(item.becados);
        holder.btnEditar.setOnClickListener(v -> listener.onEdit(item));
        holder.btnEliminar.setOnClickListener(v -> listener.onDelete(item));

        return convertView;
    }

    private static class ViewHolder {
        final TextView tvPeriodo;
        final TextView tvBecados;
        final Button btnEditar;
        final Button btnEliminar;

        ViewHolder(View v) {
            tvPeriodo = v.findViewById(R.id.tv_stock_periodo);
            tvBecados = v.findViewById(R.id.tv_stock_becados);
            btnEditar = v.findViewById(R.id.btn_stock_editar);
            btnEliminar = v.findViewById(R.id.btn_stock_eliminar);
        }
    }
}
