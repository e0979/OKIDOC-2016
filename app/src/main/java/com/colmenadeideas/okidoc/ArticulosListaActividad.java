package com.colmenadeideas.okidoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


/**
 * Actividad con la lista de artículos. Si el ancho del dispositivo es mayor o igual a 900dp, entonces
 * se incrusta el fragmento de detalle {@link ArticuloDetalleFragmento} para generar el patrón
 * Master-detail
 */
public class ArticulosListaActividad extends AppCompatActivity
        implements ArticulosListaFragmento.EscuchaFragmento {

    // ¿Hay dos paneles?
    private boolean dosPaneles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_lista_articulos);

        ((Toolbar) findViewById(R.id.toolbar)).setTitle(getTitle());

        // Verificación: ¿Existe el detalle en el layout?
        if (findViewById(R.id.contenedor_detalle_articulo) != null) {
            // Si es asi, entonces confirmar modo Master-Detail
            dosPaneles = true;

            cargarFragmentoDetalle(ModeloArticulos.ITEMS.get(0).id);
        }

        // Agregar fragmento de lista
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor_lista, ArticulosListaFragmento.crear())
                .commit();

    }

    private void cargarFragmentoDetalle(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(ArticuloDetalleFragmento.ID_ARTICULO, id);
        ArticuloDetalleFragmento fragment = new ArticuloDetalleFragmento();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_detalle_articulo, fragment)
                .commit();
    }


    @Override
    public void alSeleccionarItem(String idArticulo) {
        Log.d("ISPANEL","Non1oi");
        if (dosPaneles) {
            cargarFragmentoDetalle(idArticulo);
        } else {
            Log.d("ISPANEL","Nonoi");
            Intent intent = new Intent(this, ArticuloDetalleActividad.class);
            intent.putExtra(ArticuloDetalleFragmento.ID_ARTICULO, idArticulo);

            startActivity(intent);
        }
    }
}