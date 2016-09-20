package com.colmenadeideas.okidoc.appointments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.colmenadeideas.okidoc.ModeloArticulos;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.patients.PatientDetailActivity;
import com.colmenadeideas.okidoc.patients.PatientDetailFragment;

public class AppointmentsListActivity extends AppCompatActivity
            implements AppointmentsListFragment.FragmentListener {

        private boolean dosPaneles;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.appointments_list_oneday_activity);


            if (findViewById(R.id.detail_container) != null) {
                // Si es asi, entonces confirmar modo Master-Detail
                dosPaneles = true;

                loadDetailFragment(ModeloArticulos.ITEMS.get(0).id);
            }

            // Agregar fragmento de lista
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.list_container, AppointmentsListFragment.crear())
                    .commit();

        }
        public void setActionBarTitle(String title) {
            getSupportActionBar().setTitle(title);
        }

        private void loadDetailFragment(String patientId) {
            Bundle arguments = new Bundle();
            arguments.putString(PatientDetailFragment.PATIENT_ID, patientId);
            PatientDetailFragment fragment = new PatientDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        }


        @Override
        public void alSeleccionarItem(String patient_id) {

            if (dosPaneles) {
                Log.d("ISPANEL","YES it is 2 Panels");
                loadDetailFragment(patient_id);
            } else {
                Log.d("ISPANEL","No it's not");
                Intent intent = new Intent(this, PatientDetailActivity.class);
                intent.putExtra("patient_id", patient_id);
                startActivity(intent);
            }
        }
    }