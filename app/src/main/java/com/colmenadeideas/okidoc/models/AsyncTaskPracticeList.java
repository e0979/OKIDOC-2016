package com.colmenadeideas.okidoc.models;

import android.content.Context;
import android.os.AsyncTask;

import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.adapters.PracticeAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskPracticeList extends AsyncTask<String, Boolean, ArrayList<Practice>> {
    //Parametros, Progreso, Resultado
    Context context;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    String doctorId;

    private final TaskListener taskListener;

    public AsyncTaskPracticeList(Context a, TaskListener taskListener) {
        context = a;
        this.taskListener = taskListener;
       // this.doctorId = doctorId;
    }

    public interface TaskListener {
        void onFinished(ArrayList<Practice> result);
    }

    public ArrayList<Practice> doInBackground(String... Params) {

        ApiController api = new ApiController();
        User session = new User(context);
        JSONObject json = api.practices(session.getUid());

        ArrayList<Practice> response = new ArrayList<Practice>();
        try {
            if (json.has("practice")) {
                JSONArray practiceArray = json.getJSONArray("practice");
                for (int a = 0; a < practiceArray.length(); a++) {
                    JSONObject practice = practiceArray.getJSONObject(a);
                    String practice_id = practice.getString("id");
                    String practice_name = practice.getString("name");
                    String practice_address = practice.getString("address");

                        JSONArray scheduleArray = practice.getJSONArray("schedule");
                        for (int b = 0; b < scheduleArray.length(); b++){
                            JSONObject schedule = scheduleArray.getJSONObject(b);
                            /*String schedule_day = schedule.getString("day");
                            String schedule_ini_schedule = schedule.getString("ini_schedule");
                            String schedule_end_schedule = schedule.getString("end_schedule");
                            String schedule_quota = schedule.getString("quota");*/
                        }

                    response.add(new Practice(practice_id, practice_name, practice_address, scheduleArray, practice ));
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
    protected void onPostExecute(ArrayList<Practice> results) {
        super.onPostExecute(results);

        if(this.taskListener != null) {
            // And if it is we call the callback function on it.
            this.taskListener.onFinished(results);
        }
    }
}