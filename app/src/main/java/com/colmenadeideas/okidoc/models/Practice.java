package com.colmenadeideas.okidoc.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class Practice {

    public final String id;
    public final String name;
    public final String address;
    public final JSONArray scheduleArray;
    public final JSONObject fullData;

    public Practice(String id, String name, String address, JSONArray scheduleArray, JSONObject fullData) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.scheduleArray = scheduleArray;
        this.fullData = fullData;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public JSONArray getSchedule() { return scheduleArray; }

    public JSONObject getFullData() { return  fullData; }

}