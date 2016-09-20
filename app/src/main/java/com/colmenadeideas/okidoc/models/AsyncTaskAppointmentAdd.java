package com.colmenadeideas.okidoc.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.config;
import com.colmenadeideas.okidoc.includes.config.libs.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskAppointmentAdd extends AsyncTask<String, Boolean, JSONObject> {

    Context context;
    ProgressDialog pDialog = null;
    String step;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    String tempkey;

    private final TaskListener taskListener;

    public AsyncTaskAppointmentAdd(Context a, String b, List<NameValuePair> c, TaskListener taskListener, String tempkey) {
        context = a;
        step = b;
        params = c;
        this.taskListener = taskListener;
        this.tempkey = tempkey;
    }

    public interface TaskListener {
        void onFinished(String result);
    }

    public JSONObject doInBackground(String... Params) {

        JSONParser jsonParser = new JSONParser();
        JSONObject response = jsonParser.getJSONObjFromUrl(config.URL + "appointments/process/step/"+step+"/"+tempkey, "POST", params);
        return response;
    }

    public void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        //pDialog.setMessage("ooo");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        pDialog.setContentView(R.layout.loading_layout);

    }

    public void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        String success = null;
        try {
            success = response.getString("success");
            if (success.equals("1")) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } else {
                Log.e("NOOO", "no hay success, try again");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(this.taskListener != null) {
            // And if it is we call the callback function on it.
            this.taskListener.onFinished(success);
        }
    }

    protected void onCancelled(JSONObject response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
