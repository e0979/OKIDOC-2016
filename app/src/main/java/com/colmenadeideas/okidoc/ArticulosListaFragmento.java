package com.colmenadeideas.okidoc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;


/**
 * Fragmento especializado para la lista de artículos
 */

public class ArticulosListaFragmento extends Fragment
        implements ArticulosAdaptador.OnItemClickListener {

    private EscuchaFragmento escucha;

    public ArticulosListaFragmento() {

    }

    public static ArticulosListaFragmento crear() {
        return new ArticulosListaFragmento();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Manejo de argumentos
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_lista_articulos, container, false);

        View recyclerView = v.findViewById(R.id.reciclador);

        assert recyclerView != null;
        prepararLista((RecyclerView) recyclerView);

        return v;
    }


    private void prepararLista(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ArticulosAdaptador(ModeloArticulos.ITEMS, this));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EscuchaFragmento) {
            escucha = (EscuchaFragmento) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debes implementar EscuchaFragmento");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        escucha = null;
    }

    public void cargarDetalle(String idArticulo) {
        if (escucha != null) {
            escucha.alSeleccionarItem(idArticulo);
        }
    }

    @Override
    public void onClick(ArticulosAdaptador.ViewHolder viewHolder, String idArticulo) {
        cargarDetalle(idArticulo);
    }

    public interface EscuchaFragmento {
        void alSeleccionarItem(String idArticulo);
    }
}
