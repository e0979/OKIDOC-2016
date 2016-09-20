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


public class AsyncTaskPatient extends AsyncTask<String, Boolean, ArrayList<PatientSimple>> {
    //Parametros, Progreso, Resultado
    Context context;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    String patientId;

    private final TaskListener taskListener;

    public AsyncTaskPatient(Context a, TaskListener taskListener, String patientId) {
        context = a;
        this.taskListener = taskListener;
        this.patientId = patientId;
    }

    public interface TaskListener {
        void onFinished(ArrayList<PatientSimple> result);
    }

    public ArrayList<PatientSimple> doInBackground(String... Params) {

        ApiController api = new ApiController();
        JSONObject json = api.patient(patientId);

        ArrayList<PatientSimple> response = new ArrayList<PatientSimple>();
        try {
            if (json.has("patient")) { //TODO evaluar el lenght
                JSONArray patientArray = json.getJSONArray("patient");
                //for (int a = 0; a < patientsArray.length(); a++) {
                    JSONObject patient = patientArray.getJSONObject(0);
                    String patient_id = patient.getString("id");
                    String patient_name = patient.getString("name");
                    String patient_lastname = patient.getString("lastname");
                    String patient_email = patient.getString("email");
                    String patient_id_card = patient.getString("id_card");
                    String patient_birth = patient.getString("birth");
                    String patient_phone = patient.getString("phone");
                    String patient_avatar = patient.getString("avatar");

                    JSONObject patient_data =  new JSONObject(patient.getString("data"));

                    response.add(new PatientSimple(
                            patient_id,
                            patient_name,
                            patient_lastname,
                            patient_email,
                            patient_id_card,
                            patient_birth,
                            patient_phone,
                            patient_avatar, patient_data));
                //}
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