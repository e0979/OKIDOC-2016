package com.colmenadeideas.okidoc.controllers;

import android.util.Log;

import com.colmenadeideas.okidoc.includes.config.config;
import com.colmenadeideas.okidoc.includes.config.libs.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ApiController {

    private JSONParser jsonParser;

    private static String apiUrl = config.URL+"api/";

    public ApiController() {
        jsonParser = new JSONParser();
    }

    public JSONObject search(List<NameValuePair> params) {

        //setup array containing submitted form data
        ////List<NameValuePair> data = new List<NameValuePair>();

        String search_terms = null;

        for (NameValuePair valuePair : params) {
            String temp_var_key = valuePair.getName();
            String temp_var_value = valuePair.getValue();
            Log.d("Pair:", temp_var_key.toString()+" "+temp_var_value.toString());

            if (temp_var_key == "term") {
                search_terms = temp_var_value.toString();
            }
        }

        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"search/other/"+search_terms, "GET", params);
        //Log.e("JSON", json.toString());
        return json;
    }
    public JSONObject appointments(List<NameValuePair> params) {

        String search_terms = null;

        for (NameValuePair valuePair : params) {
            String temp_var_key = valuePair.getName();
            String temp_var_value = valuePair.getValue();
            Log.d("Pair:", temp_var_key.toString()+"/"+temp_var_value.toString());

            if (temp_var_key == "term") {
                search_terms = temp_var_value.toString();
            }
        }
       // JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl+"appointments/json/doctor/"+search_terms, "GET", params);
        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"appointments/json/doctor/"+search_terms, "URL", params);
        return json;

    }
    public JSONObject appointments(String search_terms) {

        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"appointments/json/doctor/"+search_terms, "URL", null);
        return json;
    }

    /*18042016*/
   // public JSONObject practices (List<NameValuePair> params){
    public JSONObject practices (String search_terms){
        //String search_terms = "22"; //TODO CHANGE DOCTOR ID, get it from session
        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"practices/json/doctor/"+search_terms, "URL", null);
        Log.d("Pair:","practices/json/doctor/"+search_terms);
        return json;
    }
    public JSONObject practice (String search_terms){
        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"practice/json/"+search_terms, "URL", null);
        Log.d("Pair:","practice/json/"+search_terms);
        return json;
    }

    public JSONArray availability (String search_terms){

        //api/availability/json/"+OKey+"/"+practiceID+"/hours/"+selectedDate
        JSONArray json = jsonParser.getJSONArrayFromUrl(apiUrl +"availability/json/"+search_terms, "URL", null);
        Log.d("Pair Av:", apiUrl+"availability/json/"+search_terms);
        Log.d("RESPONSE:", String.valueOf(json));
        return json;
    }
    /*//$.getJSON(globals.URL+"api/availability/json/"+OKey+"/"+practiceID+"/hours/"+selectedDate
    public JSONObject timeslots (String search_terms) {

    }*/
    public JSONObject patients (){ //THIS IS TEMP, GETS ALL PATIENTS
        //String search_terms = "22";
        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"patients/json/open/-1", "URL", null);
        Log.d("Pair:","patients/json/open/-1");
        return json;
    }

    public JSONObject patient (String id){
        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"patient/json/"+id, "URL", null);
        Log.d("Pair:","patient/json/"+id);
        return json;
    }

    public JSONArray getTempData (String search_terms) {
        JSONArray json = jsonParser.getJSONArrayFromUrl(apiUrl +"temp/json/"+search_terms, "URL", null);
        Log.d("Pair:","temp/json/"+search_terms);
        return json;
    }
    public JSONObject getTempData1 (String search_terms) {
        JSONObject json = jsonParser.getJSONObjFromUrl(apiUrl +"temp/json/"+search_terms, "URL", null);
        Log.d("Pair:","temp/json/"+search_terms);
        return json;
    }


}