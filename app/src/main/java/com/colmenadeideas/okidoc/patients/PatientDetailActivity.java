package com.colmenadeideas.okidoc.patients;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.colmenadeideas.okidoc.R;


public class PatientDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_detail_activity);

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

        // if (savedInstanceState == null) {
        // Añadir fragmento de detalle
        Bundle arguments = new Bundle();
        arguments.putString(PatientDetailFragment.PATIENT_ID,
                getIntent().getStringExtra(PatientDetailFragment.PATIENT_ID));
        PatientDetailFragment fragment = new PatientDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_container, fragment)
                .commit();
        //}
    }

}