package com.colmenadeideas.okidoc.views.practice.add;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colmenadeideas.okidoc.controllers.ApiController;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.includes.config.libs.utils.SucessDialogBoxClass;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeList;
import com.colmenadeideas.okidoc.models.AsyncTaskTempData;
import com.colmenadeideas.okidoc.models.Practice;
import com.colmenadeideas.okidoc.models.adapters.PracticeAdapter;
import com.colmenadeideas.okidoc.practices.PracticeAddActivity;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeAdd;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StepPreviewFragment extends Fragment implements AsyncTaskPracticeAdd.TaskListener, AsyncTaskTempData.TaskListener {
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    User session;
    String tempkey;
    String userID;
    JSONArray response;
    Functions functions;

    TextView textView1;     TextView textView2;         TextView textView3;        TextView textView4;
    TextView day_1;         TextView ini_schedule_1;    TextView end_schedule_1;
    TextView day_2;         TextView ini_schedule_2;    TextView end_schedule_2;
    TextView day_3;         TextView ini_schedule_3;    TextView end_schedule_3;
    TextView day_4;         TextView ini_schedule_4;    TextView end_schedule_4;
    TextView day_5;         TextView ini_schedule_5;    TextView end_schedule_5;
    TextView day_6;         TextView ini_schedule_6;    TextView end_schedule_6;
    TextView day_7;         TextView ini_schedule_7;    TextView end_schedule_7;

    LinearLayout horario1;  LinearLayout horario2;      LinearLayout horario3;      LinearLayout horario4;
    LinearLayout horario5;  LinearLayout horario6;      LinearLayout horario7;

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
        ((PracticeAddActivity)getActivity()).getViewPager().setCurrentItem(fragmentID);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            Log.d("AM I VISIBLE?", "im here"); //run only if Fragment is visible
            loadTempData();
        }
    }
    public void loadTempData() {

        //Get Formed Data Asynchronously
        AsyncTaskTempData.TaskListener listener = new AsyncTaskTempData.TaskListener() {
            @Override
            public void onFinished(JSONObject result) {

            try {
                if (result.has("data")) {

                    JSONArray dataArray = result.getJSONArray("data");
                    JSONObject data = dataArray.getJSONObject(0);


                    /* Iterator<String> iter = data.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        Log.d("key is", key.toString() + " "+key.getString(key));

                        try {
                            Object value = data.get(key);
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }
                    */

                    Iterator iter = data.keys();
                    while(iter.hasNext()){
                        String key = (String)iter.next();
                        String value = data.getString(key);
                        params.add(new BasicNameValuePair(key, value));
                    }


                    String isclinic = data.getString("isclinic");

                    if (isclinic.equals("1")){
                        String clinic = data.getString("clinic");
                        String clinic_details = data.getString("clinic_details");
                        textView1.setText(clinic);
                        textView2.setText(clinic_details);

                    } else {
                        String address = data.getString("address");
                        textView1.setText(address);
                    }
                    textView3.setText(data.getString("max_days_ahead"));
                    if (data.has("day_1")) {
                        String data_day_1 = data.getString("day_1");
                        String data_ini_schedule_1 = data.getString("ini_schedule_1");
                        String data_end_schedule_1 = data.getString("end_schedule_1");
                        day_1.setText(data_day_1);
                        ini_schedule_1.setText(data_ini_schedule_1);
                        end_schedule_1.setText(data_end_schedule_1);

                        horario1.setVisibility(View.VISIBLE);

                    }if (data.has("day_2")) {
                        String data_day_2 = data.getString("day_2");
                        String data_ini_schedule_2 = data.getString("ini_schedule_2");
                        String data_end_schedule_2 = data.getString("end_schedule_2");
                        day_2.setText(data_day_2);
                        ini_schedule_2.setText(data_ini_schedule_2);
                        end_schedule_2.setText(data_end_schedule_2);

                        horario2.setVisibility(View.VISIBLE);

                    }if (data.has("day_3")) {
                        String data_day_3 = data.getString("day_3");
                        String data_ini_schedule_3 = data.getString("ini_schedule_3");
                        String data_end_schedule_3 = data.getString("end_schedule_3");
                        day_3.setText(data_day_3);
                        ini_schedule_3.setText(data_ini_schedule_3);
                        end_schedule_3.setText(data_end_schedule_3);

                        horario3.setVisibility(View.VISIBLE);

                    }if (data.has("day_4")) {
                        String data_day_4 = data.getString("day_4");
                        String data_ini_schedule_4 = data.getString("ini_schedule_4");
                        String data_end_schedule_4 = data.getString("end_schedule_4");
                        day_4.setText(data_day_4);
                        ini_schedule_4.setText(data_ini_schedule_4);
                        end_schedule_4.setText(data_end_schedule_4);

                        horario4.setVisibility(View.VISIBLE);

                    }if (data.has("day_5")) {
                        String data_day_5 = data.getString("day_5");
                        String data_ini_schedule_5 = data.getString("ini_schedule_5");
                        String data_end_schedule_5 = data.getString("end_schedule_5");
                        day_5.setText(data_day_5);
                        ini_schedule_5.setText(data_ini_schedule_5);
                        end_schedule_5.setText(data_end_schedule_5);

                        horario5.setVisibility(View.VISIBLE);

                    }if (data.has("day_6")) {
                        String data_day_6 = data.getString("day_6");
                        String data_ini_schedule_6 = data.getString("ini_schedule_6");
                        String data_end_schedule_6 = data.getString("end_schedule_6");
                        day_6.setText(data_day_6);
                        ini_schedule_6.setText(data_ini_schedule_6);
                        end_schedule_6.setText(data_end_schedule_6);

                        horario6.setVisibility(View.VISIBLE);

                    }if (data.has("day_7")) {
                        String data_day_7 = data.getString("day_7");
                        String data_ini_schedule_7 = data.getString("ini_schedule_7");
                        String data_end_schedule_7 = data.getString("end_schedule_7");
                        day_7.setText(data_day_7);
                        ini_schedule_7.setText(data_ini_schedule_7);
                        end_schedule_7.setText(data_end_schedule_7);

                        horario7.setVisibility(View.VISIBLE);

                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            }
        };

        if (functions.isNetworkAvailable()) {
            AsyncTaskTempData getTempData = new AsyncTaskTempData(getActivity(), listener);
            getTempData.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.practice_add_preview_fragment, container,
                false);
        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        Button createButton = (Button) layoutView.findViewById(R.id.nextButtonStep);
        Button prevButton = (Button) layoutView.findViewById(R.id.prevButtonStep);

        textView1 = (TextView) layoutView.findViewById(R.id.textView1);
        textView2 = (TextView) layoutView.findViewById(R.id.textView2);
        textView3 = (TextView) layoutView.findViewById(R.id.textView3);
        textView4 = (TextView) layoutView.findViewById(R.id.textView4);

        day_1 = (TextView) layoutView.findViewById(R.id.day_1);
        ini_schedule_1 = (TextView) layoutView.findViewById(R.id.ini_schedule_1);
        end_schedule_1 = (TextView) layoutView.findViewById(R.id.end_schedule_1);

        day_2 = (TextView) layoutView.findViewById(R.id.day_2);
        ini_schedule_2 = (TextView) layoutView.findViewById(R.id.ini_schedule_2);
        end_schedule_2 = (TextView) layoutView.findViewById(R.id.end_schedule_2);

        day_3 = (TextView) layoutView.findViewById(R.id.day_3);
        ini_schedule_3 = (TextView) layoutView.findViewById(R.id.ini_schedule_3);
        end_schedule_3 = (TextView) layoutView.findViewById(R.id.end_schedule_3);

        day_4 = (TextView) layoutView.findViewById(R.id.day_4);
        ini_schedule_4 = (TextView) layoutView.findViewById(R.id.ini_schedule_4);
        end_schedule_4 = (TextView) layoutView.findViewById(R.id.end_schedule_4);

        day_5 = (TextView) layoutView.findViewById(R.id.day_5);
        ini_schedule_5 = (TextView) layoutView.findViewById(R.id.ini_schedule_5);
        end_schedule_5 = (TextView) layoutView.findViewById(R.id.end_schedule_5);

        day_6 = (TextView) layoutView.findViewById(R.id.day_6);
        ini_schedule_6 = (TextView) layoutView.findViewById(R.id.ini_schedule_6);
        end_schedule_6 = (TextView) layoutView.findViewById(R.id.end_schedule_6);

        day_7 = (TextView) layoutView.findViewById(R.id.day_7);
        ini_schedule_7 = (TextView) layoutView.findViewById(R.id.ini_schedule_7);
        end_schedule_7 = (TextView) layoutView.findViewById(R.id.end_schedule_7);

        horario1 = (LinearLayout) layoutView.findViewById(R.id.horario1);
        horario2 = (LinearLayout) layoutView.findViewById(R.id.horario2);
        horario3 = (LinearLayout) layoutView.findViewById(R.id.horario3);
        horario4 = (LinearLayout) layoutView.findViewById(R.id.horario4);
        horario5 = (LinearLayout) layoutView.findViewById(R.id.horario5);
        horario6 = (LinearLayout) layoutView.findViewById(R.id.horario6);
        horario7 = (LinearLayout) layoutView.findViewById(R.id.horario7);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSave();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToFragment(0);
            }
        });

        createButton.setTypeface(fontRegular);

        return layoutView;
    }

    private List<NameValuePair> collectForm () {

        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("tempkey", tempkey));
        params.add(new BasicNameValuePair("url", "practice.add.preview"));

        return params;
    }

    private void runSave() {

        String tempkey = session.getTempKey();

        if (functions.isNetworkAvailable()) {
            AsyncTaskPracticeAdd.TaskListener listener = new AsyncTaskPracticeAdd.TaskListener() {
                @Override
                public void onFinished(String result) {
                    if (result.equals("1")) {

                        SucessDialogBoxClass pDialog = new SucessDialogBoxClass(
                                getActivity(),
                                R.layout.success_dialogbox_practice,
                                PracticeAddActivity.class);
                        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                       // pDialog.getWindow().setBackgroundDrawable(new ColorDrawableResource(R.color.transparent));
                        pDialog.setCancelable(false);
                        pDialog.show();

                    }
                }
            };
            AsyncTaskPracticeAdd saveStep = new AsyncTaskPracticeAdd(getActivity(), "-1", params, listener, tempkey);
            saveStep.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onFinished(String result) {

    }

    @Override
    public void onFinished(JSONObject result) {

    }
}