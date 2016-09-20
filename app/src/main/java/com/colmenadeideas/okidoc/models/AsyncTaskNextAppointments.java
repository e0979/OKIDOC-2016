package com.colmenadeideas.okidoc.models;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncTaskNextAppointments extends AsyncTask<String, Boolean, ArrayList<NextAppointmentsResults>> {

    Context context;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    String doctorId;

    List<String> listDataHeader;
    HashMap<String, ArrayList<Appointment>> listDataChild;
    ArrayList<Appointment> mResultsData;
    JSONArray jsonResults;
    //NextAppointmentsResults nextAppointmentsResults;
    ArrayList<NextAppointmentsResults> nextAppointmentsResults;

    private final TaskListener taskListener;

    public AsyncTaskNextAppointments(Context a, TaskListener taskListener, String doctorId) {
        context = a;
        this.taskListener = taskListener;
        this.doctorId = doctorId;
    }

    public interface TaskListener {
        void onFinished(ArrayList<NextAppointmentsResults> result);
    }
    @Override
    protected ArrayList<NextAppointmentsResults> doInBackground(String... params) {
        //Terms
        ArrayList<NameValuePair> search_terms = new ArrayList<NameValuePair>();
        search_terms.add(new BasicNameValuePair("term", doctorId));
        //Search
        ApiController api = new ApiController();
        JSONObject json = api.appointments(search_terms);

        ArrayList<Appointment> results = new ArrayList<Appointment>();
        ArrayList<Appointment> newAppointment = new ArrayList<Appointment>();
        nextAppointmentsResults = new ArrayList<NextAppointmentsResults>();

        try{
            if(json != null) {
            //if (json.isEmpty() || json == null || json.length() < 1) {
                if (json.getString(CommonKeys.TAG_EMPTY) != null) { //!json.getString(CommonKeys.TAG_EMPTY).isEmpty()
                    int empty = json.getInt(CommonKeys.TAG_EMPTY);

                    if (empty == 1) {
                        results = null;
                    } else {

                        //Iniciar la data para el ExpandableListView
                        listDataHeader = new ArrayList<String>();
                        listDataChild = new HashMap<String, ArrayList<Appointment>>();

                        String practice_name = new String();
                        String practice_code = new String();
                        int extraAppointments = 0;
                        String id_appointment = new String();
                        ArrayList<PatientSimple> patient_current = new ArrayList<PatientSimple>();

                        try {
                            jsonResults = json.getJSONArray("dates");
                            for (int i = 0; i < jsonResults.length(); i++) {

                                JSONObject date = jsonResults.getJSONObject(i);

                                String date_date = date.getString("date_string");

                                listDataHeader.add(date_date);

                                // if(date.has("practice")) {
                                JSONArray jsonClinics = date.getJSONArray("practice");
                                Log.d("Date:", date_date + "!");
                                //ArrayList<Appointment> newAppointment = new ArrayList<Appointment>();
                                newAppointment = new ArrayList<Appointment>();
                                for (int e = 0; e < jsonClinics.length(); e++) {

                                    JSONObject practice = jsonClinics.getJSONObject(e);

                                    if (practice.has("appointments")) {

                                        practice_name = practice.getString("name");
                                        practice_code = practice.getString("id");

                                        JSONArray appointmentsArray = practice.getJSONArray("appointments");
                                        extraAppointments = appointmentsArray.length() - 3;

                                        ArrayList<PatientSimple> patientsArray = new ArrayList<PatientSimple>();
                                        for (int a = 0; a < appointmentsArray.length(); a++) {

                                            JSONObject appointment = appointmentsArray.getJSONObject(a);
                                            id_appointment = appointment.getString("id");
                                            Log.d("App :", id_appointment + "!");

                                            JSONArray patientArray = appointment.getJSONArray("patient_data");
                                            JSONObject patient = patientArray.getJSONObject(0);

                                            JSONObject patient_data =  new JSONObject(patient.getString("data"));

                                            //Add patient info to Array
                                            patientsArray.add(
                                                    new PatientSimple(
                                                            patient.getString("id"),
                                                            patient.getString("name"),
                                                            patient.getString("lastname"),
                                                            patient.getString("email"),
                                                            patient.getString("id_card"),
                                                            patient.getString("birth"),
                                                            patient.getString("phone"),
                                                            patient.getString("avatar"),
                                                            patient_data));

                                        }
                                        //For each practice
                                        //Add apointment data to AppointmentsItem Adapter

                                        newAppointment.add(new Appointment(id_appointment, practice_name, practice_code, patientsArray, extraAppointments));
                                        listDataChild.put(listDataHeader.get(i), newAppointment);
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        results = newAppointment;
                        nextAppointmentsResults.add(new NextAppointmentsResults(results, listDataChild, listDataHeader));
                    }

                }
            } else {
                Log.d("Internet Issues","");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nextAppointmentsResults;

    }

    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<NextAppointmentsResults> results) {
        super.onPostExecute(results);

        if(this.taskListener != null) {
            this.taskListener.onFinished(results);
        }
    }

}
