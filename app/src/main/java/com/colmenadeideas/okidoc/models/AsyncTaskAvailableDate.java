package com.colmenadeideas.okidoc.models;

import android.content.Context;
import android.os.AsyncTask;

import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.libs.User;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;


public class AsyncTaskAvailableDate extends AsyncTask<String, Boolean, HashSet<Date>> {

    Context context;

    private final TaskListener taskListener;

    public AsyncTaskAvailableDate(Context a, TaskListener taskListener) {
        context = a;
        this.taskListener = taskListener;
    }
    public interface TaskListener {
        void onFinished(HashSet<Date> result);
    }

    /*public ArrayList<AvailableDate> doInBackground(String... Params) {
        //api/availability/json/"+OKey+"/"+practiceID+"/hours/"+selectedDate
        ApiController api = new ApiController();

        User session = new User(context);
        String searchParams = (session.getUid())+"/"+session.getCurrentPractice()+"/days";

        JSONArray json = api.availability(searchParams);

        ArrayList<AvailableDate> response = new ArrayList<AvailableDate>();
        //Log.d("IPIPOP", json.toString());

        if (json.length() > 0) {
            for (int a = 0; a < json.length(); a++) {
                String date = Integer.toString(a);
                response.add(new AvailableDate(date));
            }
        }

        return response;
    }*/

    public HashSet<Date> doInBackground(String... Params) {
        //api/availability/json/"+OKey+"/"+practiceID+"/hours/"+selectedDate
        ApiController api = new ApiController();

        User session = new User(context);
        String searchParams = (session.getUid())+"/"+session.getCurrentPractice()+"/days";

        JSONArray json = api.availability(searchParams);

        HashSet<Date> response = new HashSet<Date>();

        if (json.length() > 0) {
            for (int a = 0; a < json.length(); a++) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String cunvertCurrentDate = json.optString(a);
                Date date = new Date();
                try {
                    date = df.parse(cunvertCurrentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                response.add(date); //add Dates to Calendar
            }
        }

        return response;
    }

    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(HashSet<Date> results) {
        super.onPostExecute(results);

        if(this.taskListener != null) {
            this.taskListener.onFinished(results);
        }
    }
}
