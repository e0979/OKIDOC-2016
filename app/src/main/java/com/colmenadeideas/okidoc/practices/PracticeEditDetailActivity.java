package com.colmenadeideas.okidoc.practices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.colmenadeideas.okidoc.ArticuloDetalleFragmento;
import com.colmenadeideas.okidoc.ArticulosListaActividad;
import com.colmenadeideas.okidoc.R;


public class PracticeEditDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_detail_activity);


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
            arguments.putString(PracticeEditDetailFragment.PRACTICE_ID,
                    getIntent().getStringExtra(PracticeEditDetailFragment.PRACTICE_ID));
            PracticeEditDetailFragment fragment = new PracticeEditDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
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