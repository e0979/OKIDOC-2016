package com.colmenadeideas.okidoc.appointments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colmenadeideas.okidoc.ArticulosAdaptador;
import com.colmenadeideas.okidoc.ModeloArticulos;
import com.colmenadeideas.okidoc.R;


public class AppointmentsListFragmentBACKUP extends Fragment
        implements ArticulosAdaptador.OnItemClickListener {

    private FragmentListener escucha;

    public AppointmentsListFragmentBACKUP() {
    }

    public static AppointmentsListFragmentBACKUP crear() {
        return new AppointmentsListFragmentBACKUP();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.appointments_list_oneday_fragment, container, false);

        View recyclerView = v.findViewById(R.id.recyclerView);
        assert recyclerView != null;
        getListReady((RecyclerView) recyclerView);

        return v;
    }

    private void getListReady(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ArticulosAdaptador(ModeloArticulos.ITEMS, this));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            escucha = (FragmentListener) context;
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

    public interface FragmentListener {
        void alSeleccionarItem(String idArticulo);
    }
}
