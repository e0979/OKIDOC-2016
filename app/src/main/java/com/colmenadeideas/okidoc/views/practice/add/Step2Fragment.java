package com.colmenadeideas.okidoc.views.practice.add;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.practices.PracticeAddActivity;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;

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
    CheckBox day_1;     CheckBox day_2;     CheckBox day_3;     CheckBox day_4;
    CheckBox day_5;     CheckBox day_6;     CheckBox day_7;

    User session;
    String tempkey;
    String userID;
    Functions functions;

    public static Step2Fragment init() {
        Step2Fragment truitonFrag = new Step2Fragment();
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        functions = new Functions(getActivity().getApplicationContext());
        session = new User(getActivity().getApplicationContext());
        tempkey = session.getTempKey();
        userID = session.getUid();
    }

    public void nextFragment() {
        ((PracticeAddActivity)getActivity()).getViewPager().setCurrentItem(2);

        hideSoftKeyboard();
    }
    public void prevFragment() {
        ((PracticeAddActivity)getActivity()).getViewPager().setCurrentItem(0);
    }

    public void hideSoftKeyboard() {
        Context contex = getActivity().getApplicationContext();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) contex.getSystemService(contex.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.practice_add_step2_fragment, container,
                false);
        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);


        Button nextButton = (Button) layoutView.findViewById(R.id.nextButtonStep);
        Button prevButton = (Button) layoutView.findViewById(R.id.prevButtonStep);

        day_1 = (CheckBox) layoutView.findViewById(R.id.day_1);
        ini_schedule_1 = (EditText) layoutView.findViewById(R.id.ini_schedule_1);
        end_schedule_1 = (EditText) layoutView.findViewById(R.id.end_schedule_1);
        day_2 = (CheckBox) layoutView.findViewById(R.id.day_2);
        ini_schedule_2 = (EditText) layoutView.findViewById(R.id.ini_schedule_2);
        end_schedule_2 = (EditText) layoutView.findViewById(R.id.end_schedule_2);
        day_3 = (CheckBox) layoutView.findViewById(R.id.day_3);
        ini_schedule_3 = (EditText) layoutView.findViewById(R.id.ini_schedule_3);
        end_schedule_3 = (EditText) layoutView.findViewById(R.id.end_schedule_3);
        day_4 = (CheckBox) layoutView.findViewById(R.id.day_4);
        ini_schedule_4 = (EditText) layoutView.findViewById(R.id.ini_schedule_4);
        end_schedule_4 = (EditText) layoutView.findViewById(R.id.end_schedule_4);
        day_5 = (CheckBox) layoutView.findViewById(R.id.day_5);
        ini_schedule_5 = (EditText) layoutView.findViewById(R.id.ini_schedule_5);
        end_schedule_5 = (EditText) layoutView.findViewById(R.id.end_schedule_5);
        day_6 = (CheckBox) layoutView.findViewById(R.id.day_6);
        ini_schedule_6 = (EditText) layoutView.findViewById(R.id.ini_schedule_6);
        end_schedule_6 = (EditText) layoutView.findViewById(R.id.end_schedule_6);
        day_7 = (CheckBox) layoutView.findViewById(R.id.day_7);
        ini_schedule_7 = (EditText) layoutView.findViewById(R.id.ini_schedule_7);
        end_schedule_7 = (EditText) layoutView.findViewById(R.id.end_schedule_7);

        day_1.setOnCheckedChangeListener(checkListener);
        day_2.setOnCheckedChangeListener(checkListener);
        day_3.setOnCheckedChangeListener(checkListener);
        day_4.setOnCheckedChangeListener(checkListener);
        day_5.setOnCheckedChangeListener(checkListener);
        day_6.setOnCheckedChangeListener(checkListener);
        day_7.setOnCheckedChangeListener(checkListener);


        nextButton.setOnClickListener(new View.OnClickListener() {
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
        prevButton.setTypeface(fontRegular);

        return layoutView;
    }

    private List<NameValuePair> collectForm () {

        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("tempkey", tempkey));
        params.add(new BasicNameValuePair("url", "practice.add.step2"));


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

    private void runSave() {
        if (functions.isNetworkAvailable()) {

            collectForm();

            AsyncTaskPracticeAdd.TaskListener listener = new AsyncTaskPracticeAdd.TaskListener() {
                @Override
                public void onFinished(String result) {
                    if (result.equals("1")) {
                        nextFragment();
                    }
                }
            };
            AsyncTaskPracticeAdd saveStep = new AsyncTaskPracticeAdd(getActivity(), "3", params, listener, tempkey);
            saveStep.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
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