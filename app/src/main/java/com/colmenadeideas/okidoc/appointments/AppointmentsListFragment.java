package com.colmenadeideas.okidoc.appointments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.Appointment;
import com.colmenadeideas.okidoc.models.AsyncTaskDayAppointments;
import com.colmenadeideas.okidoc.models.Practice;
import com.colmenadeideas.okidoc.models.adapters.AppointmentAdapter;
import com.colmenadeideas.okidoc.models.adapters.PracticeAdapter;

import java.util.ArrayList;


public class AppointmentsListFragment extends Fragment
        implements PracticeAdapter.OnItemClickListener, AsyncTaskDayAppointments.TaskListener {

    private FragmentListener escucha;

    private RecyclerView mRecyclerView;
    private AppointmentAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    ArrayList<Practice> mResultsData;

    User session;
    Functions functions;
    String selectedDate;
    String selectedPractice;

    public AppointmentsListFragment() {
    }
    public static AppointmentsListFragment crear() {
        return new AppointmentsListFragment();
    }

    public interface FragmentListener {
        void alSeleccionarItem(String idArticulo);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = savedInstanceState != null ? savedInstanceState : getActivity().getIntent().getExtras();
        selectedDate = getActivity().getIntent().getExtras().getString("date");
        selectedPractice = getActivity().getIntent().getExtras().getString("practice");

        //Date in title
        String[] datePieces = selectedDate.split("-");
        String dateYear = datePieces[0].substring(datePieces[0].length() - 2);

        ((AppointmentsListActivity) getActivity())
                .setActionBarTitle("Citas del " + datePieces[2]+"/"+datePieces[1]+"/"+dateYear);

        functions = new Functions(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView;

        // No Practices (Always inflate this as default, then update for Other)
        rootView = inflater.inflate(R.layout.appointments_list_oneday_fragment, container, false);

        FloatingActionButton addButton = (FloatingActionButton) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AppointmentsAddActivity.class);
            startActivity(intent);
            }
        });

        //Load Asynchronously
        AsyncTaskDayAppointments.TaskListener listener = new AsyncTaskDayAppointments.TaskListener() {

            @Override
            public void onFinished(ArrayList<Appointment> results) {

                if (results.isEmpty() || results == null || results.size() < 1) {
                    Toast.makeText(getActivity(), "Parece que no hay Citas! Intente otra Fecha", Toast.LENGTH_SHORT).show();
                } else {

                    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                    layoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(layoutManager);

                    mAdapter = new AppointmentAdapter(results, new AppointmentAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(Appointment item, int position) {
                             //       onItemClick(AdapterView<?> parent, View view, int position, long id)
                            String patient_id = item.getPatients().get(0).getId();

                            cargarDetalle(patient_id);

                        }
                        /*@Override
                        public void onClick(AdaptadorArticulos.ViewHolder viewHolder, String idArticulo) {
                            cargarDetalle(idArticulo);
                        }*/
                    });
                    mRecyclerView.setAdapter(mAdapter);

                }

            }

        };

        session = new User(getActivity().getApplicationContext());
        String searchTerms = session.getUid()+"/"+selectedDate+"/"+selectedDate+"/practice/"+selectedPractice;

        if (functions.isNetworkAvailable()) {

            AsyncTaskDayAppointments getAppointments = new AsyncTaskDayAppointments(getActivity(), listener, searchTerms);
            getAppointments.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }

        return rootView;
    }


    @Override
    public void onFinished(ArrayList<Appointment> result) {

    }

    @Override
    public void onItemClick(Practice item) {

    }


    public void cargarDetalle(String idArticulo) {
        if (escucha != null) {
            escucha.alSeleccionarItem(idArticulo);
        }
    }





}