package com.colmenadeideas.okidoc.views.appointment.add;


import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colmenadeideas.okidoc.MainActivity;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.appointments.AppointmentsAddActivity;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.includes.config.libs.utils.SucessDialogBoxClass;
import com.colmenadeideas.okidoc.models.AsyncTaskAppointmentAdd;
import com.colmenadeideas.okidoc.models.AsyncTaskPractice;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;
import com.colmenadeideas.okidoc.models.AsyncTaskTempData;
import com.colmenadeideas.okidoc.models.PatientSimple;
import com.colmenadeideas.okidoc.models.Practice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StepPreviewFragment extends Fragment implements AsyncTaskPracticeAdd.TaskListener, AsyncTaskPractice.TaskListener {
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    User session;
    String tempkey;
    String userID;
    Functions functions;

    RelativeLayout appointmentPreview;
    ProgressBar progressBar;

    TextView day;
    TextView month;
    TextView year;
    TextView clinic_name;
    TextView address_details;
    TextView patient_fullname;
    TextView id_card;
    TextView email;


    public static StepPreviewFragment init() {
        StepPreviewFragment truitonFrag = new StepPreviewFragment();

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

    public void backToFragment(int fragmentID) {
        ((AppointmentsAddActivity) getActivity()).getViewPager().setCurrentItem(fragmentID);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            loadTempData();
        }
    }

    public void loadTempData() {

        if (functions.isNetworkAvailable()) {
            //Get Formed Data Asynchronously
            AsyncTaskTempData.TaskListener listener = new AsyncTaskTempData.TaskListener() {
                @Override
                public void onFinished(JSONObject result) {

                    try {
                        if (result.has("data")) {

                            JSONArray dataArray = result.getJSONArray("data");
                            JSONObject data = dataArray.getJSONObject(0);

                            Iterator iter = data.keys();
                            while (iter.hasNext()) {
                                String key = (String) iter.next();
                                String value = data.getString(key);
                                params.add(new BasicNameValuePair(key, value));
                            }


                            String date = data.getString("date");
                            String[] dateSplit = date.split("-");
                            day.setText(dateSplit[2]);
                            month.setText(dateSplit[1]);
                            year.setText(dateSplit[0]);

                            patient_fullname.setText(data.getString("name") + " " + data.getString("lastname"));
                            id_card.setText(data.getString("id_card"));
                            email.setText(data.getString("email"));

                            Log.d("LO Q FALTA", data.getString("name") + " " + data.getString("lastname"));

                            AsyncTaskPractice.TaskListener listenerPractice = new AsyncTaskPractice.TaskListener() {
                                @Override
                                public void onFinished(Practice result) {

                                    clinic_name.setText(result.getName().toString());
                                    address_details.setText(result.getAddress().toString());
                                }
                            };
                            AsyncTaskPractice getPracticeData = new AsyncTaskPractice(getActivity(), listenerPractice, data.getString("id_practice"));
                            getPracticeData.execute();

                            appointmentPreview.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            };



            AsyncTaskTempData getTempData = new AsyncTaskTempData(getActivity(), listener);
            getTempData.execute();

        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.appointment_add_preview_fragment, container,
                false);
        appointmentPreview = (RelativeLayout) layoutView.findViewById(R.id.appointmentPreview);
        progressBar = (ProgressBar) layoutView.findViewById(R.id.progressBar);

        day = (TextView) layoutView.findViewById(R.id.day);
        month = (TextView) layoutView.findViewById(R.id.month);
        year = (TextView) layoutView.findViewById(R.id.year);

        clinic_name = (TextView) layoutView.findViewById(R.id.clinic_name);
        address_details = (TextView) layoutView.findViewById(R.id.address_details);

        patient_fullname = (TextView) layoutView.findViewById(R.id.patient_fullname);
        id_card = (TextView) layoutView.findViewById(R.id.id_card);
        email = (TextView) layoutView.findViewById(R.id.email);

        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        Button createButton = (Button) layoutView.findViewById(R.id.nextButtonStep);
        Button prevButton = (Button) layoutView.findViewById(R.id.prevButtonStep);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSave();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToFragment(2);
            }
        });


        createButton.setTypeface(fontRegular);

        return layoutView;
    }

    private List<NameValuePair> collectForm() {

        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("tempkey", tempkey));
        params.add(new BasicNameValuePair("url", "appointment.add.preview"));
        return params;
    }

    private void runSave() {

        if (functions.isNetworkAvailable()) {
            String tempkey = session.getTempKey();

            AsyncTaskAppointmentAdd.TaskListener listener = new AsyncTaskAppointmentAdd.TaskListener() {
                @Override
                public void onFinished(String result) {
                    if (result.equals("1")) {

                        SucessDialogBoxClass pDialog =
                                new SucessDialogBoxClass(
                                        getActivity(),
                                        R.layout.success_dialogbox_appointment,
                                        MainActivity.class);// AppointmentsAddActivity.class);
                        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                }
            };
            AsyncTaskAppointmentAdd saveStep = new AsyncTaskAppointmentAdd(getActivity(), "-1", params, listener, tempkey);
            saveStep.execute();

        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinished(String result) {
    }

    @Override
    public void onFinished(Practice result) {

    }
}
