package com.colmenadeideas.okidoc.views.appointment.add;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.appointments.AppointmentsAddActivity;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Step5Fragment extends Fragment implements AsyncTaskPracticeAdd.TaskListener {

    List<NameValuePair> params = new ArrayList<NameValuePair>();

    User session;
    String tempkey;
    String userID;
    Functions functions;

    TextView age;
    TextView height;
    TextView weight;
    RadioGroup gender;
    RadioButton gender1;
    RadioButton gender2;

    RadioGroup  blood_symbol;   RadioButton blood_symbol1;    RadioButton blood_symbol2;

    RadioGroup  blood_type;     RadioButton blood_type1;        RadioButton blood_type2;
    RadioButton blood_type3;    RadioButton blood_type4;

    public static Step5Fragment init() {
        Step5Fragment truitonFrag = new Step5Fragment();
        return truitonFrag;
    }

    public void nextFragment() {
        ((AppointmentsAddActivity) getActivity()).getViewPager().setCurrentItem(4);

        Context contex = getActivity().getApplicationContext();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) contex.getSystemService(contex.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void prevFragment() {
        ((AppointmentsAddActivity) getActivity()).getViewPager().setCurrentItem(2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        functions = new Functions(getActivity().getApplicationContext());
        session = new User(getActivity().getApplicationContext());
        tempkey = session.getTempKey();
        userID = session.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.patient_add_step2_fragment, container,
                false);

        age = (TextView) layoutView.findViewById(R.id.age);
        height = (TextView) layoutView.findViewById(R.id.height);
        weight = (TextView) layoutView.findViewById(R.id.weight);
        gender = (RadioGroup) layoutView.findViewById(R.id.gender);
        gender1  = (RadioButton) layoutView.findViewById(R.id.gender1);
        gender2  = (RadioButton) layoutView.findViewById(R.id.gender2);

        blood_symbol = (RadioGroup) layoutView.findViewById(R.id.blood_symbol);
        blood_symbol1  = (RadioButton) layoutView.findViewById(R.id.blood_symbol1);
        blood_symbol2  = (RadioButton) layoutView.findViewById(R.id.blood_symbol2);

        blood_type = (RadioGroup) layoutView.findViewById(R.id.blood_type);
        blood_type1  = (RadioButton) layoutView.findViewById(R.id.blood_type1);
        blood_type2  = (RadioButton) layoutView.findViewById(R.id.blood_type2);
        blood_type3  = (RadioButton) layoutView.findViewById(R.id.blood_type3);
        blood_type4  = (RadioButton) layoutView.findViewById(R.id.blood_type4);

        blood_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Method 1 & 2 For Getting Index of RadioButton
                int pos = blood_type.indexOfChild(layoutView.findViewById(checkedId));
                int pos1 = blood_type.indexOfChild(layoutView.findViewById(blood_type.getCheckedRadioButtonId()));

                switch (pos) {
                    case 0:
                        params.add(new BasicNameValuePair("blood_type", "A"));
                        break;
                    case 1:
                        params.add(new BasicNameValuePair("blood_type", "B"));
                        break;
                    case 2:
                        params.add(new BasicNameValuePair("blood_type", "AB"));
                        break;
                    case 3:
                        params.add(new BasicNameValuePair("blood_type", "O"));
                        break;
                }
            }
        });
        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);


        Button nextButton = (Button) layoutView.findViewById(R.id.nextButtonStep);
        Button prevButton = (Button) layoutView.findViewById(R.id.prevButtonStep);
        Button skipButton = (Button) layoutView.findViewById(R.id.skipButton);

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
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment();
            }
        });

        return layoutView;
    }

    private List<NameValuePair> collectForm () {

        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("tempkey", tempkey));
        params.add(new BasicNameValuePair("url", "appointment.add.step5"));


        params.add(new BasicNameValuePair("age", age.getText().toString()));
        params.add(new BasicNameValuePair("height", height.getText().toString()));
        params.add(new BasicNameValuePair("weight", weight.getText().toString()));

        if (gender1.isChecked()) {
            params.add(new BasicNameValuePair("gender", "M"));
        }
        if (gender2.isChecked()) {
            params.add(new BasicNameValuePair("gender", "F"));
        }

        if (blood_symbol1.isChecked()) {
            params.add(new BasicNameValuePair("blood_symbol", "+"));
        }
        if (blood_symbol2.isChecked()) {
            params.add(new BasicNameValuePair("blood_symbol", "-"));
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
            AsyncTaskPracticeAdd saveStep = new AsyncTaskPracticeAdd(getActivity(), "6", params, listener, "");
            saveStep.execute();

        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinished(String result) { }
}
