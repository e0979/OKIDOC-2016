package com.colmenadeideas.okidoc.views.appointment.add;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.appointments.AppointmentsAddActivity;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.includes.config.libs.utils.CalendarView;
import com.colmenadeideas.okidoc.models.AsyncTaskAvailableDate;
import com.colmenadeideas.okidoc.models.AsyncTaskTempSaveAdd;
import com.colmenadeideas.okidoc.models.adapters.PracticeAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Step2Fragment extends Fragment implements AsyncTaskTempSaveAdd.TaskListener,
    AsyncTaskAvailableDate.TaskListener {
    int fragVal;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    HashSet<Date> events = new HashSet<>();

    User session;
    String tempkey;
    String userID;
    Functions functions;

    private RecyclerView mRecyclerView;
    private PracticeAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    CalendarView calendarView;

    public static Step2Fragment init() {
        Step2Fragment truitonFrag = new Step2Fragment();
        return truitonFrag;
    }

    public void nextFragment() {
        ((AppointmentsAddActivity)getActivity()).getViewPager().setCurrentItem(2);
    }
    public void prevFragment() {
        ((AppointmentsAddActivity)getActivity()).getViewPager().setCurrentItem(0);
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
        final View layoutView = inflater.inflate(R.layout.appointments_add_step2_fragment, container,
                false);

        //Calendar
        events.add(new Date());

        calendarView = ((CalendarView)layoutView.findViewById(R.id.calendar_view));
        calendarView.updateCalendar(events);

        // assign event handler
        calendarView.setEventHandler(new CalendarView.EventHandler(){
            @Override
            public void onDayLongPress(Date date) {
                //get in proper format
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = format1.format(date);
                String hasSelectedPractice = session.getCurrentPractice();
                params.add(new BasicNameValuePair("id_practice", hasSelectedPractice));
                params.add(new BasicNameValuePair("date", dateString));
                runSave();
            }

        });

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

        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        nextButton.setTypeface(fontRegular);
        prevButton.setTypeface(fontRegular);

        return layoutView;

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            Log.d("AM I VISIBLE?", "im here"); //run only if Fragment is visible
            loadDates();
        }
    }

    private List<NameValuePair> collectForm () {

        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("tempkey", tempkey));
        params.add(new BasicNameValuePair("url", "appointment.add.step2"));

        return params;
    }

    private void runSave() {

        if (functions.isNetworkAvailable()) {
            collectForm();
            String tempkey = session.getTempKey();

            AsyncTaskTempSaveAdd.TaskListener listener = new AsyncTaskTempSaveAdd.TaskListener() {
                @Override
                public void onFinished(String result) {

                    if (result.equals("1")) {
                        nextFragment();
                    }
                }
            };
            AsyncTaskTempSaveAdd saveStep = new AsyncTaskTempSaveAdd(getActivity(),"appointments", "4", params, listener, tempkey);
            saveStep.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    public void loadDates() {

        if (functions.isNetworkAvailable()) {
            //Load Dates Asynchronously
            AsyncTaskAvailableDate.TaskListener listener = new AsyncTaskAvailableDate.TaskListener() {
                @Override
                public void onFinished(HashSet<Date> results) {
                    calendarView.updateCalendar(results);

                }
            };

            AsyncTaskAvailableDate getAvailableDates = new AsyncTaskAvailableDate(getActivity(), listener);
            getAvailableDates.execute();

        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinished(String result) { }

    @Override
    public void onFinished(HashSet<Date> result) {

    }
}
