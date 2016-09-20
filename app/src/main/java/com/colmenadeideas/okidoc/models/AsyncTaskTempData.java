package com.colmenadeideas.okidoc.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.config;
import com.colmenadeideas.okidoc.includes.config.libs.JSONParser;
import com.colmenadeideas.okidoc.includes.config.libs.User;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AsyncTaskTempData  extends AsyncTask<String, Boolean, JSONObject> {
    //Params, Progress, Result
    Context context;
    ProgressDialog pDialog = null;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    User session;

    private final TaskListener taskListener;
    public AsyncTaskTempData(Context a, TaskListener taskListener) {
        context = a;
        this.taskListener = taskListener;
    }

    public interface TaskListener {
        void onFinished(JSONObject result);
    }

    public JSONObject doInBackground(String... Params) {

        ApiController api = new ApiController();
        session = new User(context);
        String params = session.getUid()+"/"+session.getTempKey();
        JSONObject response = api.getTempData1(params);

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

        if(this.taskListener != null) {
            // And if it is we call the callback function on it.
            this.taskListener.onFinished(response);
            pDialog.dismiss();
        }
    }

    protected void onCancelled(JSONObject response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}
