package com.colmenadeideas.okidoc.appointments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.Appointment;
import com.colmenadeideas.okidoc.models.PatientSimple;
import com.colmenadeideas.okidoc.models.adapters.AppointmentsDateAdapter;
import com.colmenadeideas.okidoc.patients.PatientAddActivity;
import com.colmenadeideas.okidoc.practices.PracticeAddActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AppointmentsNextFragmentBACKUP extends Fragment {

    User session;
    String doctorId;
    ArrayList<Appointment> mResultsData;
    JSONArray jsonResults;

    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, ArrayList<Appointment>> listDataChild;

    public AppointmentsNextFragmentBACKUP() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new User(getActivity());
        session.checkSession("AppointmentsNextFragment");

        doctorId = session.getUid();
        Log.d("UID", doctorId);

        DataSearchLoader dataSearchLoader = new DataSearchLoader();
        dataSearchLoader.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        if (mResultsData == null || mResultsData.isEmpty()) {

            rootView = inflater.inflate(R.layout.appointments_next_fragment, container, false);

            //FAB ACTIONS
            final FloatingActionButton addPracticeButton = (FloatingActionButton) rootView.findViewById(R.id.add_practice);
            addPracticeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PracticeAddActivity.class);
                    startActivity(intent);
                }
            });

            final FloatingActionButton addPatientButton = (FloatingActionButton) rootView.findViewById(R.id.add_patient);
            addPatientButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PatientAddActivity.class);
                    startActivity(intent);
                }
            });

            final FloatingActionButton addAppointmentButton = (FloatingActionButton) rootView.findViewById(R.id.add_appointment);
            addAppointmentButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AppointmentsAddActivity.class);
                    startActivity(intent);
                }
            });

        } else { // No Appointments
            rootView = inflater.inflate(R.layout.appointments_empty_fragment, container, false);

            final Button addAppointmentButton = (Button) rootView.findViewById(R.id.add_practice_button);
            addAppointmentButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AppointmentsAddActivity.class);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }

    private class DataSearchLoader extends AsyncTask<String, Boolean, ArrayList<Appointment>> {

        @Override
        protected ArrayList<Appointment> doInBackground(String... params) {
            //Terms
            ArrayList<NameValuePair> search_terms = new ArrayList<NameValuePair>();
            search_terms.add(new BasicNameValuePair("term", doctorId));
            //Search
            ApiController api = new ApiController();
            JSONObject json = api.appointments(search_terms);

            ArrayList<Appointment> results = new ArrayList<Appointment>();
            ArrayList<Appointment> newAppointment = new ArrayList<Appointment>();

            try{
                if(json.getString(CommonKeys.TAG_EMPTY) != null){ //!json.getString(CommonKeys.TAG_EMPTY).isEmpty()
                    int empty = json.getInt(CommonKeys.TAG_EMPTY);

                    if(empty == 1) {
                        results = null;
                    } else {

                        //Iniciar la data para el ExpandableListView
                        listDataHeader = new ArrayList<String>();
                        listDataChild = new  HashMap<String, ArrayList<Appointment>>();
                        String practice_name = new String();
                        String practice_code = new String();
                        int extraAppointments = 0;
                        String id_appointment = new String();
                        ArrayList<PatientSimple> patient_current = new ArrayList<PatientSimple>();

                        try {
                            jsonResults = json.getJSONArray("dates");
                            for (int i =0; i < jsonResults.length(); i++ ) {

                                JSONObject date = jsonResults.getJSONObject(i);

                                String date_date = date.getString("date_string");

                                listDataHeader.add(date_date);

                                // if(date.has("practice")) {
                                JSONArray jsonClinics = date.getJSONArray("practice");
                                Log.d("Date:", date_date + "!");
                                //ArrayList<Appointment> newAppointment = new ArrayList<Appointment>();
                                newAppointment = new ArrayList<Appointment>();
                                for (int e =0; e < jsonClinics.length(); e++ ) {

                                    JSONObject practice = jsonClinics.getJSONObject(e);

                                    if(practice.has("appointments")) {

                                        practice_name = i+" "+practice.getString("name");
                                        practice_code = practice.getString("id");
                                        Log.d("Pratice :", i +") "+practice_name+"!");

                                        JSONArray appointmentsArray = practice.getJSONArray("appointments");
                                        extraAppointments = appointmentsArray.length()-3;

                                       // ArrayList<ArrayList<String>> patientsArray = new ArrayList<ArrayList<String>>();
                                         ArrayList<PatientSimple> patientsArray = new ArrayList<PatientSimple>();
                                        for (int a =0; a < appointmentsArray.length(); a++ ) {

                                            JSONObject appointment = appointmentsArray.getJSONObject(a);
                                            id_appointment = appointment.getString("id");
                                            Log.d("App :", id_appointment+"!");

                                            JSONArray patientArray = appointment.getJSONArray("patient_data");
                                            JSONObject patient = patientArray.getJSONObject(0);

                                            //Add patient info to Array
                                           /* patient_current = new ArrayList<PatientSimple>();
                                            patient_current.add(patient.getString("id"));
                                            patient_current.add(patient.getString("name"));
                                            patient_current.add(patient.getString("name")); //this should be profile pic
                                            patientsArray.add(patient_current);*/
                                        }
                                        //For each practice
                                        //Add apointment data to AppointmentsItem Adapter

                                        newAppointment.add(new Appointment(id_appointment,practice_name,practice_code, patientsArray,extraAppointments));
                                        listDataChild.put(listDataHeader.get(i), newAppointment);
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        results = newAppointment;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return results;
        }

        protected void onPostExecute(ArrayList<Appointment> results) {
            super.onPostExecute(results);
            mResultsData = results;

            if (mResultsData  == null || mResultsData.isEmpty()) {
                // No Appointments
            } else {
                updateResults();
            }
        }

        private void updateResults() {
            expListView = (ExpandableListView) getActivity().findViewById(R.id.appointments_dates_list);
            AppointmentsDateAdapter listAdapter = new AppointmentsDateAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);

            //To Expand all children
            int count = listAdapter.getGroupCount();
            for (int position = 1; position <= count; position++) {
                expListView.expandGroup(position - 1);
            }
            // Listview on child click listener
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                /* String appointmentsItemCode =  listDataChild.get(
                       listDataHeader.get(groupPosition)).get(childPosition).getAppointmentCode();
                */
                    String practiceCode =  listDataChild.get(
                            listDataHeader.get(groupPosition)).get(childPosition).getPracticeCode();

                    // System.out.println("Choosen Date = : " + listDataHeader.get(groupPosition));
                    Intent intent = new Intent(getActivity(), AppointmentsListActivity.class); //TODO "inflate" as Fragment, not as activity, so it can mantain menu sides
                    //Pass extra info to next activity
                    Bundle b = new Bundle();
                    b.putString("sea", "men");
                    b.putString("date", listDataHeader.get(groupPosition));
                    b.putString("practice", practiceCode);
                    //Add the bundle to the intent
                    intent.putExtras(b);
                    startActivity(intent);

                    return false;
                }
            });

        }

    }

}