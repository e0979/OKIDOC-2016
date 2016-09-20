package com.colmenadeideas.okidoc.views.patient.add;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;
import com.colmenadeideas.okidoc.patients.PatientAddActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;




public class Step1Fragment extends Fragment implements AsyncTaskPracticeAdd.TaskListener {

    int fragVal;
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    User session;
    String tempkey;

    EditText name;
    EditText lastname;
    EditText id_card;
    EditText birth;
    EditText cellphone;
    EditText phone;
    EditText email;


    public static Step1Fragment init(int val) {
        Step1Fragment truitonFrag = new Step1Fragment();
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;

        session = new User(getActivity().getApplicationContext());
        tempkey = session.getTempKey();
    }

    public void nextFragment() {
        ((PatientAddActivity)getActivity()).getViewPager().setCurrentItem(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layoutView = inflater.inflate(R.layout.patient_add_step1_fragment, container,
                false);

        Button nextButtonStep = (Button) layoutView.findViewById(R.id.nextButtonStep);


        nextButtonStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSave();
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

        params.add(new BasicNameValuePair("user_id", "22"));
        params.add(new BasicNameValuePair("id_doctor", "22"));
        params.add(new BasicNameValuePair("role", "doctor"));
        params.add(new BasicNameValuePair("form", "patient"));
        params.add(new BasicNameValuePair("url", "url"));

        Functions functions = new Functions();
        String tempKey = functions.generateTempKey("ss");
        params.add(new BasicNameValuePair("tempkey", tempKey));

        return params;
    }

    private void runSave() {
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
        AsyncTaskPracticeAdd saveStep = new AsyncTaskPracticeAdd(getActivity(), "2", params, listener, "");
        saveStep.execute();
    }

    @Override
    public void onFinished(String result) {
    }
}