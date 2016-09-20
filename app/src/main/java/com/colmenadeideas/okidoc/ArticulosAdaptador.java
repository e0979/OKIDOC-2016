package com.colmenadeideas.okidoc;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class ArticulosAdaptador extends RecyclerView.Adapter<ArticulosAdaptador.ViewHolder> {

    private final List<ModeloArticulos.Articulo> valores;

    public ArticulosAdaptador(List<ModeloArticulos.Articulo> items,
                              OnItemClickListener escuchaClicksExterna) {
        valores = items;
        this.escuchaClicksExterna = escuchaClicksExterna;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_item_list_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = valores.get(position);
        holder.viewTitulo.setText(valores.get(position).titulo);
        holder.viewResumen.setText(valores.get(position).descripcion);
       // holder.viewFecha.setText(valores.get(position).fecha);
        Glide.with(holder.itemView.getContext())
                .load(holder.item.urlMiniatura)
                .thumbnail(0.1f)
                .centerCrop()
                .into(holder.viewMiniatura);

    }

    @Override
    public int getItemCount() {
        if (valores != null) {
            return valores.size() > 0 ? valores.size() : 0;
        } else {
            return 0;
        }
    }


    private String obtenerIdArticulo(int posicion) {
        if (posicion != RecyclerView.NO_POSITION) {
            return valores.get(posicion).id;
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public final TextView viewTitulo;
        public final TextView viewResumen;
        //public final TextView viewFecha;
        public final ImageView viewMiniatura;

        public ModeloArticulos.Articulo item;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            viewTitulo = (TextView) view.findViewById(R.id.patientName);
            viewResumen = (TextView) view.findViewById(R.id.appointmentTime);
            //viewFecha = (TextView) view.findViewById(R.id.fecha);
            viewMiniatura = (ImageView) view.findViewById(R.id.miniatura);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            escuchaClicksExterna.onClick(this, obtenerIdArticulo(getAdapterPosition()));
        }
    }


    public interface OnItemClickListener {
        public void onClick(ViewHolder viewHolder, String idArticulo);
    }

    private OnItemClickListener escuchaClicksExterna;
}