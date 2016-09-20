package com.colmenadeideas.okidoc.views.practice.add;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.practices.PracticeAddActivity;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class Step1Fragment extends Fragment implements AsyncTaskPracticeAdd.TaskListener {

    int fragVal;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    User session;
    String tempkey;
    Functions functions;

    RelativeLayout clinicAddressLayout;
    RelativeLayout regularAddressLayout;
    RadioGroup radioGroup;

    RadioButton isclinic1;  RadioButton isclinic2;
    AutoCompleteTextView clinic;
    EditText clinic_details;
    EditText address;
    EditText clinic_address;

    public static Step1Fragment init(int val) {
        Step1Fragment truitonFrag = new Step1Fragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
        functions = new Functions (getActivity().getApplicationContext());
        session = new User(getActivity().getApplicationContext());
        tempkey = session.getTempKey();
    }

    public void nextFragment() {
        ((PracticeAddActivity)getActivity()).getViewPager().setCurrentItem(1);

        hideSoftKeyboard();
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
        final View layoutView = inflater.inflate(R.layout.practice_add_step1_fragment, container,
                false);

        Button nextButtonStep = (Button) layoutView.findViewById(R.id.nextButtonStep);

        clinicAddressLayout = (RelativeLayout) layoutView.findViewById(R.id.clinicAddress);
        regularAddressLayout = (RelativeLayout) layoutView.findViewById(R.id.regularAddress);
        clinicAddressLayout.setVisibility(RelativeLayout.GONE);
        regularAddressLayout.setVisibility(RelativeLayout.GONE);

        radioGroup = (RadioGroup) layoutView.findViewById(R.id.isclinic);
        isclinic1  = (RadioButton) layoutView.findViewById(R.id.isclinic1);
        isclinic2  = (RadioButton) layoutView.findViewById(R.id.isclinic2);

        //Clinic
        clinic = (AutoCompleteTextView) layoutView.findViewById(R.id.clinic);
        clinic_details = (EditText) layoutView.findViewById(R.id.clinic_details);
        clinic_address = (EditText) layoutView.findViewById(R.id.clinic_address);
        // Not Clinic
        address = (EditText) layoutView.findViewById(R.id.address);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Method 1 & 2 For Getting Index of RadioButton
                int pos = radioGroup.indexOfChild(layoutView.findViewById(checkedId));
                int pos1 = radioGroup.indexOfChild(layoutView.findViewById(radioGroup.getCheckedRadioButtonId()));

                switch (pos) {
                    case 0:
                        clinicAddressLayout.setVisibility(RelativeLayout.VISIBLE);
                        regularAddressLayout.setVisibility(RelativeLayout.GONE);

                        if (params.contains("isclinic")) {
                            params.remove("isclinic");
                            params.remove("address");
                        }
                        break;
                    case 1:
                        clinicAddressLayout.setVisibility(RelativeLayout.GONE);
                        regularAddressLayout.setVisibility(RelativeLayout.VISIBLE);

                        if (params.contains("isclinic")) {
                            params.remove("isclinic");
                            params.remove("clinic");
                            params.remove("clinic_details");
                            params.remove("address");
                        }
                        break;
                }
            }
        });

        nextButtonStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSave();
            }
        });

        clinic.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    clinic_details.requestFocus();
                }
                return false;
            }
        });

        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        nextButtonStep.setTypeface(fontRegular);

        return layoutView;

    }
    private List<NameValuePair> collectForm () {


        String userID = session.getUid();

        params.add(new BasicNameValuePair("user_id", userID));
        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("role", session.getRole()));
        params.add(new BasicNameValuePair("form", "practice"));
        params.add(new BasicNameValuePair("url", "practice.add.step1"));

        params.add(new BasicNameValuePair("tempkey", tempkey));

        if (isclinic1.isChecked()) {
            params.add(new BasicNameValuePair("isclinic", "1"));
            params.add(new BasicNameValuePair("clinic", clinic.getText().toString()));
            params.add(new BasicNameValuePair("clinic_details", clinic_details.getText().toString()));
            //params.add(new BasicNameValuePair("address", clinic_address.getText().toString()));
            params.add(new BasicNameValuePair("address", " "));
        }
        if (isclinic2.isChecked()) {
            params.add(new BasicNameValuePair("isclinic", "0"));
            params.add(new BasicNameValuePair("address", address.getText().toString()));
        }

        return params;
    }

    private void runSave() {

        if (functions.isNetworkAvailable()) {
            //Get Data
            collectForm();

            AsyncTaskPracticeAdd.TaskListener listener = new AsyncTaskPracticeAdd.TaskListener() {
                @Override
                public void onFinished(String result) {
                if (result.equals("1")) {
                    nextFragment();
                }
                }
            };
            //Save Asynchronously
            AsyncTaskPracticeAdd saveStep = new AsyncTaskPracticeAdd(getActivity(), "2", params, listener, tempkey);
            saveStep.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinished(String result) {
    }
}