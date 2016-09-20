package com.colmenadeideas.okidoc.includes.config;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.colmenadeideas.okidoc.PanelActivity;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.includes.config.libs.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    Button btnLogin;
    EditText inputEmail;
    EditText inputPassword;
    TextView btnRegister;

    private ProgressDialog pDialog;
    User session;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.login_activity);


        session = new User(getApplicationContext());
        //  session.logoutUser("LoginActivity");

        session.checkSession("LoginActivity");
        Boolean activeSession = session.activeSession();
        //Is already Logged in
        if (activeSession == true) {

           /* Intent identify = new Intent(getApplicationContext(), DashboardActivity.class);*/
            Intent identify = new Intent(getApplicationContext(), PanelActivity.class);
            identify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(identify);
            finish();

        } else {

            //Import all assets
            inputEmail = (EditText) findViewById(R.id.field_username);
            inputPassword = (EditText) findViewById(R.id.field_password);
            btnLogin = (Button) findViewById(R.id.login_button);
            btnRegister = (TextView) findViewById(R.id.search_button);

            //Font
            Typeface fontLight =  Typeface.createFromAsset(getAssets(),"fonts/Lato_Light.ttf");
            Typeface fontRegular =  Typeface.createFromAsset(getAssets(),"fonts/Lato_Regular.ttf");
            Typeface fontBold =  Typeface.createFromAsset(getAssets(), "fonts/Lato_Bold.ttf");
            inputEmail.setTypeface(fontRegular);
            inputPassword.setTypeface(fontRegular);
            btnLogin.setTypeface(fontBold);
            btnRegister.setTypeface(fontRegular);

            btnLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    new AttemptLogin().execute();
                }
            });
        }

    }



    class AttemptLogin extends AsyncTask<String, String, String> {
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage(getString(R.string.message_processing));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            String response;
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String role;
            Intent identify = new Intent(getApplicationContext(), PanelActivity.class);//IdentifyActivity.class); //SET A DEFAULT INTENT

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(email,password);


            //Check for login response
            try{
                if(json.getString(CommonKeys.TAG_SUCCESS) != null){
                    // Log.d("Login Response", json.toString());
                    success = json.getInt(CommonKeys.TAG_SUCCESS);
                    response = json.getString(CommonKeys.KEY_RESPONSE);

                    if(success == 1) {
                        // Launch Dashboard Screen
                        Log.d("Login Success", json.getString(CommonKeys.TAG_SUCCESS));
                        //Set sessions in sessions
                        session.set("loggedIn", true);

                        JSONObject userData = json.getJSONObject(CommonKeys.KEY_USER);

                        session.set(CommonKeys.KEY_UID, userData.getString(CommonKeys.KEY_UID));
                        session.set(CommonKeys.KEY_EMAIL, email);
                        session.set(CommonKeys.KEY_PASSWORD, password);
                        role = userData.getString(CommonKeys.KEY_ROLE);
                        session.set(CommonKeys.KEY_ROLE, role);


                        if (role.equals("doctor")) {
                            /*identify = new Intent(getApplicationContext(), DashboardActivity.class);*/
                            identify = new Intent(getApplicationContext(), PanelActivity.class);
                            //DELIA, Este es el sitio que usa para identify
                        }
                        if (role.equals("patient")) {
                            //TODO SET HOME CLASS FOR PATIENT
                            //identify = new Intent(getApplicationContext(), IdentifyActivity.class);
                        }
                        //identify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(identify);
                        finish();

                    } else {
                        Log.d("Login Failure!", json.getString(CommonKeys.KEY_RESPONSE));
                        return response;

                    }

                   /*
                    if(Integer.parseInt(res) == 1){

                        // user successfully logged in
                        // Store user details in SQLite Database
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        // Clear all previous data in database
                        userFunction.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));

                    */
                }

            } catch (JSONException e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}