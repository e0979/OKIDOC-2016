package com.colmenadeideas.okidoc.models;

import android.content.Context;
import android.os.AsyncTask;

import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


 public class AsyncTaskDayAppointments extends AsyncTask<String, Boolean, ArrayList<Appointment>> {
        //Parametros, Progreso, Resultado
        Context context;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String searchTerms;
        private final TaskListener taskListener;

        String practice_name;
        String practice_code;

        public AsyncTaskDayAppointments(Context a, TaskListener taskListener, String searchTerms) {
            context = a;
            this.taskListener = taskListener;
            this.searchTerms = searchTerms;
        }

        public interface TaskListener {
            void onFinished(ArrayList<Appointment> result);
        }

        public ArrayList<Appointment> doInBackground(String... Params) {

            ApiController api = new ApiController();
            JSONObject json = api.appointments(searchTerms);

            ArrayList<Appointment> response         = new ArrayList<Appointment>();
            ArrayList<Appointment>  appointments    = new ArrayList<Appointment>();


            try {
                if (json.getString(CommonKeys.TAG_EMPTY) != null) { //!json.getString(CommonKeys.TAG_EMPTY).isEmpty()
                    int empty = json.getInt(CommonKeys.TAG_EMPTY);

                    if (empty == 1) {
                        response = null;
                    } else {

                        int extraAppointments = 0;
                        String id_appointment = new String();

                        JSONObject jsonResults = json.getJSONArray("dates").getJSONObject(0);
                        String date_date = jsonResults.getString("date_string");
                        JSONObject practice = jsonResults.getJSONArray("practice").getJSONObject(0);

                        if(practice.has("appointments")) {

                            practice_name = practice.getString("name");
                            practice_code = practice.getString("id");

                            JSONArray appointmentsArray = practice.getJSONArray("appointments");



                            for (int a = 0; a < appointmentsArray.length(); a++ ) {

                                JSONObject appointmentData = appointmentsArray.getJSONObject(a);

                                id_appointment = appointmentData.getString("id");

                                JSONArray patientData = appointmentData.getJSONArray("patient_data");
                                ArrayList<PatientSimple> patient = new ArrayList<PatientSimple>();
                                for (int p = 0; p < patientData.length(); p++) {
                                    JSONObject patientInfo = patientData.getJSONObject(p);

                                    //JSONObject patientData = appointmentData.getJSONArray("patient_data").getJSONObject(0);
                                    JSONObject patient_data =  new JSONObject(patientInfo.getString("data"));

                                    patient.add(new PatientSimple(
                                            patientInfo.getString("id"),
                                            patientInfo.getString("name"),
                                            patientInfo.getString("lastname"),
                                            patientInfo.getString("email"),
                                            patientInfo.getString("id_card"),
                                            patientInfo.getString("birth"),
                                            patientInfo.getString("phone"),
                                            patientInfo.getString("avatar"), patient_data));
                                }
                                appointments.add(new Appointment(id_appointment, practice_name, practice_code, patient, extraAppointments));

                            }

                        }
                        response = appointments;
                    }
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

            return response;
        }

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Appointment> results) {
            super.onPostExecute(results);

            if(this.taskListener != null) {
                // And if it is we call the callback function on it.
                this.taskListener.onFinished(results);
            }
        }
    }