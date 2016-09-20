package com.colmenadeideas.okidoc.models;

import android.content.Context;
import android.os.AsyncTask;

import com.colmenadeideas.okidoc.controllers.ApiController;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AsynctaskPatientsSimple extends AsyncTask<String, Boolean, ArrayList<PatientSimple>> {
        //Parametros, Progreso, Resultado
        Context context;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        private final TaskListener taskListener;

        public AsynctaskPatientsSimple(Context a, TaskListener taskListener, String doctorId) {
            context = a;
            this.taskListener = taskListener;
        }

        public interface TaskListener {
            void onFinished(ArrayList<PatientSimple> result);
        }

        public ArrayList<PatientSimple> doInBackground(String... Params) {

            ApiController api = new ApiController();
            JSONObject json = api.patients();

            ArrayList<PatientSimple> response = new ArrayList<PatientSimple>();
            try {
                if (json.has("patients")) { //TODO evaluar el lenght
                    JSONArray patientsArray = json.getJSONArray("patients");
                    for (int a = 0; a < patientsArray.length(); a++) {
                        JSONObject patients = patientsArray.getJSONObject(a);
                        String patient_id = patients.getString("id");
                        String patient_name = patients.getString("name");
                        String patient_lastname = patients.getString("lastname");
                        String patient_email = patients.getString("email");
                        String patient_id_card = patients.getString("id_card");
                        String patient_birth = patients.getString("birth");
                        String patient_phone = patients.getString("phone");
                        String patient_avatar = patients.getString("avatar");

                        JSONObject patient_data =  new JSONObject(patients.getString("data"));

                        response.add(new PatientSimple(
                                patient_id,
                                patient_name,
                                patient_lastname,
                                patient_email,
                                patient_id_card,
                                patient_birth,
                                patient_phone,
                                patient_avatar, patient_data));
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
        protected void onPostExecute(ArrayList<PatientSimple> results) {
            super.onPostExecute(results);

            if(this.taskListener != null) {
                this.taskListener.onFinished(results);
            }
        }
    }