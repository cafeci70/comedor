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
 * Unified row adapter for daily menu tables (student read-only view and
 * secretary view with edit/delete actions).
 */
public class MenuHorarioAdapter extends BaseAdapter {

    public interface OnMenuActionListener {
        void onEdit(MenuHorarioItem item);

        void onDelete(MenuHorarioItem item);
    }

    private final LayoutInflater inflater;
    private final List<MenuHorarioItem> items;
    private final OnMenuActionListener listener;

    public MenuHorarioAdapter(Context context, List<MenuHorarioItem> items, OnMenuActionListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MenuHorarioItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_menu_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuHorarioItem item = getItem(position);
        holder.tvFecha.setText(item.fecha);
        holder.tvDesayuno.setText(item.desayuno);
        holder.tvAlmEntrada.setText(item.almEntrada);
        holder.tvAlmPostre.setText(item.almPostre);
        holder.tvAlmuerzo.setText(item.almuerzo);
        holder.tvAlmBebida.setText(item.almBebida);
        holder.tvCenEntrada.setText(item.cenEntrada);
        holder.tvCenPostre.setText(item.cenPostre);
        holder.tvCena.setText(item.cena);
        holder.tvCenBebida.setText(item.cenBebida);

        if (listener != null) {
            holder.colEditar.setVisibility(View.VISIBLE);
            holder.colEliminar.setVisibility(View.VISIBLE);
            holder.btnEditar.setOnClickListener(v -> listener.onEdit(item));
            holder.btnEliminar.setOnClickListener(v -> listener.onDelete(item));
        } else {
            holder.colEditar.setVisibility(View.GONE);
            holder.colEliminar.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        final TextView tvFecha;
        final TextView tvDesayuno;
        final TextView tvAlmEntrada;
        final TextView tvAlmPostre;
        final TextView tvAlmuerzo;
        final TextView tvAlmBebida;
        final TextView tvCenEntrada;
        final TextView tvCenPostre;
        final TextView tvCena;
        final TextView tvCenBebida;
        final View colEditar;
        final View colEliminar;
        final Button btnEditar;
        final Button btnEliminar;

        ViewHolder(View v) {
            tvFecha = v.findViewById(R.id.tv_fecha);
            tvDesayuno = v.findViewById(R.id.tv_desayuno);
            tvAlmEntrada = v.findViewById(R.id.tv_alm_entrada);
            tvAlmPostre = v.findViewById(R.id.tv_alm_postre);
            tvAlmuerzo = v.findViewById(R.id.tv_almuerzo);
            tvAlmBebida = v.findViewById(R.id.tv_alm_bebida);
            tvCenEntrada = v.findViewById(R.id.tv_cen_entrada);
            tvCenPostre = v.findViewById(R.id.tv_cen_postre);
            tvCena = v.findViewById(R.id.tv_cena);
            tvCenBebida = v.findViewById(R.id.tv_cen_bebida);
            colEditar = v.findViewById(R.id.col_editar);
            colEliminar = v.findViewById(R.id.col_eliminar);
            btnEditar = v.findViewById(R.id.btn_row_editar);
            btnEliminar = v.findViewById(R.id.btn_row_eliminar);
        }
    }
}
