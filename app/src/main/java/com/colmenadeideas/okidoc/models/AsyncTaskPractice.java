package com.colmenadeideas.okidoc.models;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.libs.User;


public class AsyncTaskPractice extends AsyncTask<String, Boolean, Practice> {
    //Parametros, Progreso, Resultado
    Context context;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    String clinicId;
    Practice response;

    private final TaskListener taskListener;

    public AsyncTaskPractice(Context a, TaskListener taskListener, String clinicId) {
        context = a;
        this.taskListener = taskListener;
        this.clinicId = clinicId;
    }

    public interface TaskListener {
        void onFinished(Practice result);
    }

    public Practice doInBackground(String... Params) {

        ApiController api = new ApiController();
        User session = new User(context);
        //String searchParams = (session.getCurrentPractice()+"/doctor/"+session.getUid());
        String searchParams = (clinicId+"/doctor/"+session.getUid());

        JSONObject json = api.practice(searchParams);

        try {
            if (json.has("practice")) {
                JSONArray practiceArray = json.getJSONArray("practice");
                for (int a = 0; a < practiceArray.length(); a++) {
                    JSONObject practice = practiceArray.getJSONObject(a);
                    String practice_id = practice.getString("id");
                    String practice_name = practice.getString("name");
                    String practice_address = practice.getString("address");

                    JSONArray scheduleArray = practice.getJSONArray("schedule");
                    response = new Practice(practice_id, practice_name, practice_address, scheduleArray, practice);
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
    protected void onPostExecute(Practice results) {
        super.onPostExecute(results);

        if(this.taskListener != null) {
            // And if it is we call the callback function on it.
            this.taskListener.onFinished(results);
        }
    }
}