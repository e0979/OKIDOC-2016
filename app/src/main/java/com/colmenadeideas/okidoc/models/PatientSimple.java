package com.colmenadeideas.okidoc.models;

import org.json.JSONObject;

public class PatientSimple {

    public final String id;
    public final String name;
    public final String lastname;
    public final String email;
    public final String id_card;
    public final String birth;
    public final String phone;
    public final String avatar;
    public final JSONObject data;

    public PatientSimple(String id,
                         String name,
                         String lastname,
                         String email,
                         String id_card,
                         String birth,
                         String phone,
                         String avatar,
                         JSONObject data) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.id_card = id_card;
        this.birth = birth;
        this.phone = phone;
        this.avatar = avatar;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() { return avatar; }

    public JSONObject getData() { return data; }


}