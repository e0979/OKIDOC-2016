package com.colmenadeideas.okidoc.includes.config.libs;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

import java.util.UUID;
import java.util.regex.Pattern;

public class Functions {

    Context context;

    public Functions () {}

    public Functions (Context context) {
        this.context = context;
    }



    public static String generateTempKey(String username) {

        UUID uniqueRandom = UUID.randomUUID();
        String tempkey = username+uniqueRandom.toString();
        return tempkey;
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }


    //Validacion de Datos
    public boolean validName(String name){
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        return patron.matcher(name).matches() || name.length() > 30;
    }
    public boolean validPhone(String phoneNumber){
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public boolean validEmail(String emailAdress){
        return Patterns.EMAIL_ADDRESS.matcher(emailAdress).matches();
    }

}
