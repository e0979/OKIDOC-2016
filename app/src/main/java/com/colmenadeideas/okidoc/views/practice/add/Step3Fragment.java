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
import android.widget.NumberPicker;
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

public class Step3Fragment extends Fragment implements AsyncTaskPracticeAdd.TaskListener {

    NumberPicker max_days_ahead;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    User session;
    String tempkey;
    String userID;
    Functions functions;

    public static Step3Fragment init() {
        Step3Fragment truitonFrag = new Step3Fragment();
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
        ((PracticeAddActivity)getActivity()).getViewPager().setCurrentItem(3);
        hideSoftKeyboard();
    }
    public void prevFragment() {
        ((PracticeAddActivity)getActivity()).getViewPager().setCurrentItem(1);
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
        final View layoutView = inflater.inflate(R.layout.practice_add_step3_fragment, container,
                false);
        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        max_days_ahead = (NumberPicker) layoutView.findViewById(R.id.max_days_ahead);
        max_days_ahead.setMinValue(15);// restricted number to minimum value i.e 1
        max_days_ahead.setMaxValue(90);
        //max_days_ahead.setWrapSelectorWheel(true);

        Button nextButton = (Button) layoutView.findViewById(R.id.nextButtonStep);
        Button prevButton = (Button) layoutView.findViewById(R.id.prevButtonStep);

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
        params.add(new BasicNameValuePair("url", "practice.add.step3"));

        params.add(new BasicNameValuePair("max_days_ahead", Integer.toString(max_days_ahead.getValue())));
        params.add(new BasicNameValuePair("manage_time_slots", "0")); //Don't manage

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
            AsyncTaskPracticeAdd saveStep = new AsyncTaskPracticeAdd(getActivity(), "4", params, listener, tempkey);
            saveStep.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinished(String result) { }
}
