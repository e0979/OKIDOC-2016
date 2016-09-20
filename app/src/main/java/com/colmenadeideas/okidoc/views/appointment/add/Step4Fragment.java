package com.colmenadeideas.okidoc.views.appointment.add;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.appointments.AppointmentsAddActivity;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.AsyncTaskTempSaveAdd;
import com.colmenadeideas.okidoc.models.AsynctaskPatientsSimple;
import com.colmenadeideas.okidoc.models.PatientSimple;
import com.colmenadeideas.okidoc.models.adapters.PatientsAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Step4Fragment extends Fragment implements AsyncTaskTempSaveAdd.TaskListener, AsynctaskPatientsSimple.TaskListener  {
    int fragVal;
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    User session;
    String tempkey;
    String userID;
    Functions functions;

    AutoCompleteTextView nameAutocomplete;
    AutoCompleteTextView lastnameAutocomplete;
    AutoCompleteTextView emailAutocomplete;
    ArrayList<PatientSimple> mAutoSearch;

    TextView id_cardField;
    TextView birthField;
    TextView phoneField;
    TextView cellphoneField;
    TextView patientIdField;
    Button clearButton;

    TextWatcher filterTextWatcher;


    public static Step4Fragment init() {
        Step4Fragment truitonFrag = new Step4Fragment();
        return truitonFrag;
    }

    public void nextFragment() {
        ((AppointmentsAddActivity)getActivity()).getViewPager().setCurrentItem(3);

        Context contex = getActivity().getApplicationContext();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) contex.getSystemService(contex.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void prevFragment() {
        ((AppointmentsAddActivity)getActivity()).getViewPager().setCurrentItem(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;

        functions = new Functions (getActivity().getApplicationContext());
        session = new User(getActivity().getApplicationContext());
        tempkey = session.getTempKey();
        userID = session.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.patient_add_step1_fragment, container,
                false);

        final Button nextButtonStep = (Button) layoutView.findViewById(R.id.nextButtonStep);
        Button prevButtonStep = (Button) layoutView.findViewById(R.id.prevButtonStep);
        prevButtonStep.setVisibility(View.VISIBLE);


        nextButtonStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSave();
            }
        });
        prevButtonStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFragment();
            }
        });

        //Load Asynchronously
        if (functions.isNetworkAvailable()) {
            AsynctaskPatientsSimple.TaskListener listener = new AsynctaskPatientsSimple.TaskListener() {

                @Override
                public void onFinished(ArrayList<PatientSimple> results) {

                    Log.d("Patients Total:", Integer.toString(results.size()));
                    mAutoSearch = results;

                    if (results.isEmpty() || results == null || results.size() < 1) {
                       // Toast.makeText(getActivity(), "patients, Failed to fetch data!", Toast.LENGTH_SHORT).show();
                    } else {

                        //Get Patients just once
                        PatientsAdapter adapter =
                                new PatientsAdapter(getActivity().getApplicationContext(), R.layout.autocomplete_search_withimage, results, "");
                        PatientsAdapter adapterEmail =
                                new PatientsAdapter(getActivity().getApplicationContext(), R.layout.autocomplete_search_withimage, results, "email");
                        PatientsAdapter adapterLastname =
                                new PatientsAdapter(getActivity().getApplicationContext(), R.layout.autocomplete_search_withimage, results, "lastname");

                        nameAutocomplete.setAdapter(adapter);
                        lastnameAutocomplete.setAdapter(adapterLastname);
                        emailAutocomplete.setAdapter(adapterEmail);

                    }
                }
            };
            session = new User(getActivity().getApplicationContext());
            AsynctaskPatientsSimple getPatients = new AsynctaskPatientsSimple(getActivity(), listener, session.getUid());
            getPatients.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }

        nameAutocomplete =
                (AutoCompleteTextView) layoutView.findViewById(R.id.name);
        lastnameAutocomplete =
                (AutoCompleteTextView) layoutView.findViewById(R.id.lastname);
        emailAutocomplete =
                (AutoCompleteTextView) layoutView.findViewById(R.id.email);

        id_cardField    = (TextView) layoutView.findViewById(R.id.id_card);
        birthField      = (TextView) layoutView.findViewById(R.id.birth);
        phoneField      = (TextView) layoutView.findViewById(R.id.phone);
        cellphoneField  = (TextView) layoutView.findViewById(R.id.cellphone);
        patientIdField  = (TextView) layoutView.findViewById(R.id.patient_id);

        clearButton = (Button) layoutView.findViewById(R.id.clearButton);

        //nameAutocomplete.addTextChangedListener(filterTextWatcher);
        nameAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                autoFillSelectedData(view);
            }

        });

        lastnameAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoFillSelectedData(view);
            }

        });
        emailAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoFillSelectedData(view);
            }

        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameAutocomplete.setText("");
                lastnameAutocomplete.setText("");
                emailAutocomplete.setText("");
                id_cardField.setText("");
                birthField.setText("");
                phoneField.setText("");
                patientIdField.setText("");
            }
        });


        emailAutocomplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    nextButtonStep.setEnabled(true);
                } else {
                    nextButtonStep.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        nextButtonStep.setTypeface(fontRegular);

        return layoutView;

    }

    private void autoFillSelectedData(/*int position*/ View view) {

       /* String selectedName = mAutoSearch.get(position).name.toString();
        String selectedLastname = mAutoSearch.get(position).lastname.toString();
        String selectedEmail = mAutoSearch.get(position).email.toString();
        String selectedIdcard = mAutoSearch.get(position).email.toString();
        String selectedBirth = mAutoSearch.get(position).birth.toString();
        String selectedPhone = mAutoSearch.get(position).phone.toString();

        nameAutocomplete.setText(selectedName);
        lastnameAutocomplete.setText(selectedLastname);
        emailAutocomplete.setText(selectedEmail);*/

        RelativeLayout ll = (RelativeLayout) view;
        LinearLayout rl = (LinearLayout) ll.getChildAt(2);
        TextView patient_id = (TextView) rl.getChildAt(0);
        TextView name       = (TextView) rl.getChildAt(1);
        TextView lastname   = (TextView) rl.getChildAt(2);
        TextView email      = (TextView) rl.getChildAt(3);
        TextView id_card    = (TextView) rl.getChildAt(4);
        TextView birth      = (TextView) rl.getChildAt(5);
        TextView phone      = (TextView) rl.getChildAt(6);

        //Log.d("PICKED I1D", name.getText().toString());

        nameAutocomplete.setText(name.getText().toString());
        lastnameAutocomplete.setText(lastname.getText().toString());
        emailAutocomplete.setText(email.getText().toString());
        id_cardField.setText(id_card.getText().toString());
        birthField.setText(birth.getText().toString());
        phoneField.setText(phone.getText().toString());
        patientIdField.setText(patient_id.getText().toString());
    }


    private List<NameValuePair> collectForm () {

        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("tempkey", tempkey));
        params.add(new BasicNameValuePair("url", "appointment.add.step4"));

        String hasPatientID = patientIdField.getText().toString();

        if (hasPatientID.equals("")) {
            //get Clean info
            params.add(new BasicNameValuePair("name", nameAutocomplete.getText().toString()));
            params.add(new BasicNameValuePair("lastname", lastnameAutocomplete.getText().toString()));
            params.add(new BasicNameValuePair("email", emailAutocomplete.getText().toString()));
            params.add(new BasicNameValuePair("id_card", id_cardField.getText().toString()));
            params.add(new BasicNameValuePair("birth", birthField.getText().toString()));
            params.add(new BasicNameValuePair("phone", phoneField.getText().toString()));
            params.add(new BasicNameValuePair("cellphone", cellphoneField.getText().toString()));

        } else {
            params.add(new BasicNameValuePair("patient_id", hasPatientID)); //TODO Update other fields, if different? block fields when selected?
            //Send for Show in Preview
            params.add(new BasicNameValuePair("name", nameAutocomplete.getText().toString()));
            params.add(new BasicNameValuePair("lastname", lastnameAutocomplete.getText().toString()));
            params.add(new BasicNameValuePair("id_card", id_cardField.getText().toString()));
            params.add(new BasicNameValuePair("email", emailAutocomplete.getText().toString()));

            //update?
            params.add(new BasicNameValuePair("id_card", id_cardField.getText().toString()));
            params.add(new BasicNameValuePair("birth", birthField.getText().toString()));
            params.add(new BasicNameValuePair("phone", phoneField.getText().toString()));
            params.add(new BasicNameValuePair("cellphone", cellphoneField.getText().toString()));

        }

        return params;
    }

    private void runSave() {

        if (functions.isNetworkAvailable()) {

            collectForm();
            AsyncTaskTempSaveAdd.TaskListener listener = new AsyncTaskTempSaveAdd.TaskListener() {
                @Override
                public void onFinished(String result) {
                    if (result.equals("1")) {
                        nextFragment();
                    }
                }
            };
            AsyncTaskTempSaveAdd saveStep = new AsyncTaskTempSaveAdd(getActivity(), "appointments", "5", params, listener, tempkey);
            saveStep.execute();

        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinished(String result) { }


    @Override
    public void onFinished(ArrayList<PatientSimple> result) {

    }
}
