package com.colmenadeideas.okidoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Actividad que muestra el detalle del artículo seleccionado en {@link ArticulosListaActividad}
 */
public class ArticuloDetalleActividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle_articulo);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalle);
        setSupportActionBar(toolbar);
        */
        // Verificación: ¿La app se está ejecutando en un teléfono?
        if (!getResources().getBoolean(R.bool.esTablet)) {
            // Mostrar Up Button
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        if (savedInstanceState == null) {
            // Añadir fragmento de detalle
            Bundle arguments = new Bundle();
            arguments.putString(ArticuloDetalleFragmento.ID_ARTICULO,
                    getIntent().getStringExtra(ArticuloDetalleFragmento.ID_ARTICULO));
            ArticuloDetalleFragmento fragment = new ArticuloDetalleFragmento();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contenedor_detalle_articulo, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_articulo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ArticulosListaActividad.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}