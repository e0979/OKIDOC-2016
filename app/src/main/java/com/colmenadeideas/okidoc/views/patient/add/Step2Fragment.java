package com.colmenadeideas.okidoc.views.patient.add;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;
import com.colmenadeideas.okidoc.patients.PatientAddActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Step2Fragment extends Fragment implements AsyncTaskPracticeAdd.TaskListener  {

    List<NameValuePair> params = new ArrayList<NameValuePair>();

    EditText ini_schedule_1;    EditText end_schedule_1;
    EditText ini_schedule_2;    EditText end_schedule_2;
    EditText ini_schedule_3;    EditText end_schedule_3;
    EditText ini_schedule_4;    EditText end_schedule_4;
    EditText ini_schedule_5;    EditText end_schedule_5;
    EditText ini_schedule_6;    EditText end_schedule_6;
    EditText ini_schedule_7;    EditText end_schedule_7;

    public static Step2Fragment init(int val) {
        Step2Fragment truitonFrag = new Step2Fragment();
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    public void nextFragment() {
        ((PatientAddActivity)getActivity()).getViewPager().setCurrentItem(2);
    }
    public void prevFragment() {
        ((PatientAddActivity)getActivity()).getViewPager().setCurrentItem(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.patient_add_step2_fragment, container,
                false);
        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);


        Button nextButton = (Button) layoutView.findViewById(R.id.nextButtonStep);
        Button prevButton = (Button) layoutView.findViewById(R.id.prevButtonStep);

       /* final CheckBox day_1 = (CheckBox) layoutView.findViewById(R.id.day_1);
        ini_schedule_1 = (EditText) layoutView.findViewById(R.id.ini_schedule_1);
        end_schedule_1 = (EditText) layoutView.findViewById(R.id.end_schedule_1);
        final CheckBox day_2 = (CheckBox) layoutView.findViewById(R.id.day_2);
        ini_schedule_2 = (EditText) layoutView.findViewById(R.id.ini_schedule_2);
        end_schedule_2 = (EditText) layoutView.findViewById(R.id.end_schedule_2);
        final CheckBox day_3 = (CheckBox) layoutView.findViewById(R.id.day_3);
        ini_schedule_3 = (EditText) layoutView.findViewById(R.id.ini_schedule_3);
        end_schedule_3 = (EditText) layoutView.findViewById(R.id.end_schedule_3);
        final CheckBox day_4 = (CheckBox) layoutView.findViewById(R.id.day_4);
        ini_schedule_4 = (EditText) layoutView.findViewById(R.id.ini_schedule_4);
        end_schedule_4 = (EditText) layoutView.findViewById(R.id.end_schedule_4);
        final CheckBox day_5 = (CheckBox) layoutView.findViewById(R.id.day_5);
        ini_schedule_5 = (EditText) layoutView.findViewById(R.id.ini_schedule_5);
        end_schedule_5 = (EditText) layoutView.findViewById(R.id.end_schedule_5);
        final CheckBox day_6 = (CheckBox) layoutView.findViewById(R.id.day_6);
        ini_schedule_6 = (EditText) layoutView.findViewById(R.id.ini_schedule_6);
        end_schedule_6 = (EditText) layoutView.findViewById(R.id.end_schedule_6);
        final CheckBox day_7 = (CheckBox) layoutView.findViewById(R.id.day_7);
        ini_schedule_7 = (EditText) layoutView.findViewById(R.id.ini_schedule_7);
        end_schedule_7 = (EditText) layoutView.findViewById(R.id.end_schedule_7);

        day_1.setOnCheckedChangeListener(checkListener);
        day_2.setOnCheckedChangeListener(checkListener);
        day_3.setOnCheckedChangeListener(checkListener);
        day_4.setOnCheckedChangeListener(checkListener);
        day_5.setOnCheckedChangeListener(checkListener);
        day_6.setOnCheckedChangeListener(checkListener);
        day_7.setOnCheckedChangeListener(checkListener);*/


        /*nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSave();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFragment();
            }
        });

        nextButton.setTypeface(fontRegular);
        prevButton.setTypeface(fontRegular);*/

        return layoutView;
    }

    private List<NameValuePair> collectForm () {

        params.add(new BasicNameValuePair("user_id", "22"));
        params.add(new BasicNameValuePair("id_doctor", "22"));
        params.add(new BasicNameValuePair("role", "doctor"));
        params.add(new BasicNameValuePair("form", "practice"));
        params.add(new BasicNameValuePair("url", "url"));
        params.add(new BasicNameValuePair("tempkey", "15454"));

        return params;
    }

    private void runSave() {

        collectForm();
        AsyncTaskPracticeAdd.TaskListener listener = new AsyncTaskPracticeAdd.TaskListener() {
            @Override
            public void onFinished(String result) {
                if (result.equals("1")) {
                    nextFragment();
                }
            }
        };
        AsyncTaskPracticeAdd saveStep = new AsyncTaskPracticeAdd(getActivity(), "3", params, listener, "");
        saveStep.execute();
    }

    @Override
    public void onFinished(String result) { }




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
}