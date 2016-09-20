package com.colmenadeideas.okidoc.practices;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.includes.config.libs.utils.SucessDialogBoxClass;
import com.colmenadeideas.okidoc.models.AsyncTaskPractice;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeEditSave;
import com.colmenadeideas.okidoc.models.Practice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PracticeEditDetailFragment extends Fragment {

    public static final String PRACTICE_ID = "practice_id";
    String practice_id;

    Functions functions;
    User session;
    String userID;
    Context mContext;

    EditText name;  TextView addressDetails;
    EditText maxDaysAhead;
    Switch manage_time_slotsSwitch;

    CheckBox day_1;     CheckBox day_2;     CheckBox day_3;
    CheckBox day_4;     CheckBox day_5;     CheckBox day_6; CheckBox day_7;

    EditText ini_schedule_1;    EditText end_schedule_1;
    EditText ini_schedule_2;    EditText end_schedule_2;
    EditText ini_schedule_3;    EditText end_schedule_3;
    EditText ini_schedule_4;    EditText end_schedule_4;
    EditText ini_schedule_5;    EditText end_schedule_5;
    EditText ini_schedule_6;    EditText end_schedule_6;
    EditText ini_schedule_7;    EditText end_schedule_7;

    LinearLayout quota_per_day;
    EditText Quota_1;   EditText Quota_2;   EditText Quota_3;
    EditText Quota_4;   EditText Quota_5;   EditText Quota_6;

    private TextInputLayout tilName;
    private TextInputLayout tilAddressDetails;
    private TextInputLayout tilEmail;
    EditText tilMaxDaysAhead;

    List<NameValuePair> params = new ArrayList<NameValuePair>();

    NestedScrollView scroll;


    public PracticeEditDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        functions = new Functions(mContext);
        session = new User(mContext);
        userID = session.getUid();

        if (getArguments().containsKey(PRACTICE_ID)) {
            practice_id = getArguments().getString(PRACTICE_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.practice_edit_detail_fragment, container, false);

        FloatingActionButton editButton = (FloatingActionButton) layoutView.findViewById(R.id.editButton);

        scroll = (NestedScrollView) layoutView.findViewById(R.id.scroll);

        name = (EditText) layoutView.findViewById(R.id.name);
        addressDetails = (EditText) layoutView.findViewById(R.id.address_details);

        maxDaysAhead = (EditText) layoutView.findViewById(R.id.max_days_ahead);

        manage_time_slotsSwitch = (Switch) layoutView.findViewById(R.id.manage_time_slots);

        day_1 = (CheckBox) layoutView.findViewById(R.id.day_1);
        day_2 = (CheckBox) layoutView.findViewById(R.id.day_2);
        day_3 = (CheckBox) layoutView.findViewById(R.id.day_3);
        day_4 = (CheckBox) layoutView.findViewById(R.id.day_4);
        day_5 = (CheckBox) layoutView.findViewById(R.id.day_5);
        day_6 = (CheckBox) layoutView.findViewById(R.id.day_6);
        day_7 = (CheckBox) layoutView.findViewById(R.id.day_7);

        day_1.setOnCheckedChangeListener(checkListener);
        day_2.setOnCheckedChangeListener(checkListener);
        day_3.setOnCheckedChangeListener(checkListener);
        day_4.setOnCheckedChangeListener(checkListener);
        day_5.setOnCheckedChangeListener(checkListener);
        day_6.setOnCheckedChangeListener(checkListener);
        day_7.setOnCheckedChangeListener(checkListener);

        ini_schedule_1 = (EditText) layoutView.findViewById(R.id.ini_schedule_1);
        ini_schedule_2 = (EditText) layoutView.findViewById(R.id.ini_schedule_2);
        ini_schedule_3 = (EditText) layoutView.findViewById(R.id.ini_schedule_3);
        ini_schedule_4 = (EditText) layoutView.findViewById(R.id.ini_schedule_4);
        ini_schedule_5 = (EditText) layoutView.findViewById(R.id.ini_schedule_5);
        ini_schedule_6 = (EditText) layoutView.findViewById(R.id.ini_schedule_6);
        ini_schedule_7 = (EditText) layoutView.findViewById(R.id.ini_schedule_7);

        end_schedule_1 = (EditText) layoutView.findViewById(R.id.end_schedule_1);
        end_schedule_2 = (EditText) layoutView.findViewById(R.id.end_schedule_2);
        end_schedule_3 = (EditText) layoutView.findViewById(R.id.end_schedule_3);
        end_schedule_4 = (EditText) layoutView.findViewById(R.id.end_schedule_4);
        end_schedule_5 = (EditText) layoutView.findViewById(R.id.end_schedule_5);
        end_schedule_6 = (EditText) layoutView.findViewById(R.id.end_schedule_6);
        end_schedule_7 = (EditText) layoutView.findViewById(R.id.end_schedule_7);

        quota_per_day   = (LinearLayout) layoutView.findViewById(R.id.quota_per_day);

        Quota_1 = (EditText) layoutView.findViewById(R.id.Quota_1);
        Quota_2 = (EditText) layoutView.findViewById(R.id.Quota_2);
        Quota_3 = (EditText) layoutView.findViewById(R.id.Quota_3);
        Quota_4 = (EditText) layoutView.findViewById(R.id.Quota_4);
        Quota_5 = (EditText) layoutView.findViewById(R.id.Quota_5);
        Quota_6 = (EditText) layoutView.findViewById(R.id.Quota_6);



        // Referencias TILs
        tilName = (TextInputLayout) layoutView.findViewById(R.id.tilName);
        tilAddressDetails = (TextInputLayout) layoutView.findViewById(R.id.tilAddressDetails);


       // tilEmail = (TextInputLayout) layoutView.findViewById(R.id.tilEmail);


        editButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                validateSave();
                //runSave();
            }
        });


        loadPractice();

        return layoutView;
    }

    private void loadPractice(){
        //Load Asynchronously
        if (functions.isNetworkAvailable()) {
            AsyncTaskPractice.TaskListener listener = new AsyncTaskPractice.TaskListener() {
                @Override
                public void onFinished(Practice result) {

                    if (result == null) {
                        Toast.makeText(getActivity(), "An error ocurred fetching data!", Toast.LENGTH_SHORT).show();
                    } else {
                        fillDatainView(result);
                    }
                }
            };

            AsyncTaskPractice getPracticeData = new AsyncTaskPractice(getActivity(), listener, practice_id);
            getPracticeData.execute();

        } else {
            Toast.makeText(mContext, R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    private void fillDatainView(Practice result) {

        JSONObject practice = result.getFullData();
        String isClinic = null;

        try {
           // isClinic = practice.getString("id_clinic");
           //if (isClinic == "null") {

            String var_name = practice.getString("name");


            name.setText(result.getName());
            addressDetails.setText(result.getAddress());

            maxDaysAhead.setText(practice.getString("max_days_ahead"));

            //SCHEDULE
            JSONArray schedule = result.getSchedule();

            if (schedule.length() > 0) {
                for (int a = 0; a < schedule.length(); a++) {
                    JSONObject scheduleDay = schedule.getJSONObject(a);
                    String day = scheduleDay.getString("day_fullname");
                    String ini_time = scheduleDay.getString("ini_schedule");
                    String end_time = scheduleDay.getString("end_schedule");
                    String quota = scheduleDay.getString("quota");
                    if (quota == "null") {
                        quota = "";
                    }

                    switch (day) {
                        case "Mon":
                            day_1.setChecked(true);
                            ini_schedule_1.setText(ini_time);
                            end_schedule_1.setText(end_time);
                            Quota_1.setText(quota);
                            break;

                        case "Tue":
                            day_2.setChecked(true);
                            ini_schedule_2.setText(ini_time);
                            end_schedule_2.setText(end_time);
                            Quota_2.setText(quota);
                            break;

                        case "Wed":
                            day_3.setChecked(true);
                            ini_schedule_3.setText(ini_time);
                            end_schedule_3.setText(end_time);
                            Quota_3.setText(quota);
                            break;

                        case "Thu":
                            day_4.setChecked(true);
                            ini_schedule_4.setText(ini_time);
                            end_schedule_4.setText(end_time);
                            Quota_4.setText(quota);
                            break;

                        case "Fri":
                            day_5.setChecked(true);
                            ini_schedule_5.setText(ini_time);
                            end_schedule_5.setText(end_time);
                            Quota_5.setText(quota);
                            break;

                        case "Sat":
                            day_6.setChecked(true);
                            ini_schedule_6.setText(ini_time);
                            end_schedule_6.setText(end_time);
                            Quota_6.setText(quota);
                            break;

                        case "Sun":
                            day_7.setChecked(true);
                            ini_schedule_7.setText(ini_time);
                            end_schedule_7.setText(end_time);
                            break;
                    }
                }
            }


            //QUOTA
            String manage_time_slots = practice.getString("manage_time_slots");

            if (manage_time_slots == "1"){
                manage_time_slotsSwitch.setChecked(true); //set the switch to ON
                quota_per_day.setVisibility(View.VISIBLE);

               //Quota_6.requestFocus();

                quota_per_day.post(new Runnable() {
                    @Override
                    public void run() {

                        scroll.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });

            } else {
                manage_time_slotsSwitch.setChecked(false); //set the switch to OFF
                quota_per_day.setVisibility(View.GONE);
            }

            //attach a listener to check for changes in state
            manage_time_slotsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if(isChecked){
                        quota_per_day.setVisibility(View.VISIBLE);
                    }else{
                        quota_per_day.setVisibility(View.GONE);
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.day_1:
                    if (isChecked == true) {
                        ini_schedule_1.setVisibility(RelativeLayout.VISIBLE);
                        end_schedule_1.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        ini_schedule_1.setVisibility(RelativeLayout.GONE);
                        end_schedule_1.setVisibility(RelativeLayout.GONE);
                    }
                    break;
                case R.id.day_2:
                    if (isChecked == true) {
                        ini_schedule_2.setVisibility(RelativeLayout.VISIBLE);
                        end_schedule_2.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        ini_schedule_2.setVisibility(RelativeLayout.GONE);
                        end_schedule_2.setVisibility(RelativeLayout.GONE);
                    }
                    break;
                case R.id.day_3:
                    if (isChecked == true) {
                        ini_schedule_3.setVisibility(RelativeLayout.VISIBLE);
                        end_schedule_3.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        ini_schedule_3.setVisibility(RelativeLayout.GONE);
                        end_schedule_3.setVisibility(RelativeLayout.GONE);
                    }
                    break;
                case R.id.day_4:
                    if (isChecked == true) {
                        ini_schedule_4.setVisibility(RelativeLayout.VISIBLE);
                        end_schedule_4.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        ini_schedule_4.setVisibility(RelativeLayout.GONE);
                        end_schedule_4.setVisibility(RelativeLayout.GONE);
                    }
                    break;
                case R.id.day_5:
                    if (isChecked == true) {
                        ini_schedule_5.setVisibility(RelativeLayout.VISIBLE);
                        end_schedule_5.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        ini_schedule_5.setVisibility(RelativeLayout.GONE);
                        end_schedule_5.setVisibility(RelativeLayout.GONE);
                    }
                    break;
                case R.id.day_6:
                    if (isChecked == true) {
                        ini_schedule_6.setVisibility(RelativeLayout.VISIBLE);
                        end_schedule_6.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        ini_schedule_6.setVisibility(RelativeLayout.GONE);
                        end_schedule_6.setVisibility(RelativeLayout.GONE);
                    }
                    break;
                case R.id.day_7:
                    if (isChecked == true) {
                        ini_schedule_7.setVisibility(RelativeLayout.VISIBLE);
                        end_schedule_7.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        ini_schedule_7.setVisibility(RelativeLayout.GONE);
                        end_schedule_7.setVisibility(RelativeLayout.GONE);
                    }
                    break;
            }
        }
    };


    private List<NameValuePair> collectForm () {

        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("url", "practice.update"));


        params.add(new BasicNameValuePair("name", name.getText().toString()));
        params.add(new BasicNameValuePair("addressDetails", addressDetails.getText().toString()));
        params.add(new BasicNameValuePair("max_days_ahead", maxDaysAhead.getText().toString()));
        //params.add(new BasicNameValuePair("", ""));


        if (manage_time_slotsSwitch.isChecked()) {
            params.add(new BasicNameValuePair("manage_time_slots", "1"));
        } else{
            params.add(new BasicNameValuePair("manage_time_slots", "0"));
        }

        if (day_1.isChecked()) {
            params.add(new BasicNameValuePair("day_1", "LUN"));
            params.add(new BasicNameValuePair("ini_schedule_1", ini_schedule_1.getText().toString()));
            params.add(new BasicNameValuePair("end_schedule_1", end_schedule_1.getText().toString()));
        }
        if (day_2.isChecked()) {
            params.add(new BasicNameValuePair("day_2", "MAR"));
            params.add(new BasicNameValuePair("ini_schedule_2", ini_schedule_2.getText().toString()));
            params.add(new BasicNameValuePair("end_schedule_2", end_schedule_2.getText().toString()));
        }
        if (day_3.isChecked()) {
            params.add(new BasicNameValuePair("day_3", "MIE"));
            params.add(new BasicNameValuePair("ini_schedule_3", ini_schedule_3.getText().toString()));
            params.add(new BasicNameValuePair("end_schedule_3", end_schedule_3.getText().toString()));
        }
        if (day_4.isChecked()) {
            params.add(new BasicNameValuePair("day_4", "JUE"));
            params.add(new BasicNameValuePair("ini_schedule_4", ini_schedule_4.getText().toString()));
            params.add(new BasicNameValuePair("end_schedule_4", end_schedule_4.getText().toString()));
        }
        if (day_5.isChecked()) {
            params.add(new BasicNameValuePair("day_5", "VIE"));
            params.add(new BasicNameValuePair("ini_schedule_5", ini_schedule_5.getText().toString()));
            params.add(new BasicNameValuePair("end_schedule_5", end_schedule_5.getText().toString()));
        }
        if (day_6.isChecked()) {
            params.add(new BasicNameValuePair("day_6", "SAB"));
            params.add(new BasicNameValuePair("ini_schedule_6", ini_schedule_6.getText().toString()));
            params.add(new BasicNameValuePair("end_schedule_6", end_schedule_6.getText().toString()));
        }
        if (day_7.isChecked()) {
            params.add(new BasicNameValuePair("day_7", "DOM"));
            params.add(new BasicNameValuePair("ini_schedule_7", ini_schedule_7.getText().toString()));
            params.add(new BasicNameValuePair("end_schedule_7", end_schedule_7.getText().toString()));
        }

        return params;
    }


    private void validateSave() {
        String nameValid = tilName.getEditText().getText().toString();
        String addressValid = tilAddressDetails.getEditText().getText().toString();

        boolean a = functions.validName(nameValid);
        boolean b = functions.validPhone(addressValid);

       // if (a && b && c) {
        if (a && b) { // Is Valid, go to next Action
            runSave();
        }
    }

    private void runSave() {

        if (functions.isNetworkAvailable()) {
            collectForm();
            AsyncTaskPracticeEditSave.TaskListener listener = new AsyncTaskPracticeEditSave.TaskListener() {
                @Override
                public void onFinished(String result){
                    if (result.equals("1")){
                        Log.d("GU", "listo!");
                    }

                }
            };
            AsyncTaskPracticeEditSave saveStep = new AsyncTaskPracticeEditSave(getActivity(), practice_id, params, listener);
            saveStep.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }


    }

}
