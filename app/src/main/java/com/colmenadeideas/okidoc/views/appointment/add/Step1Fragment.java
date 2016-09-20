package com.colmenadeideas.okidoc.views.appointment.add;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.appointments.AppointmentsAddActivity;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.includes.config.libs.utils.CustomAlertBox;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeList;
import com.colmenadeideas.okidoc.models.AsyncTaskTempSaveAdd;
import com.colmenadeideas.okidoc.models.Practice;
import com.colmenadeideas.okidoc.models.adapters.PracticeAdapter;
import com.colmenadeideas.okidoc.practices.PracticeAddActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class Step1Fragment extends Fragment implements AsyncTaskPracticeList.TaskListener, AsyncTaskTempSaveAdd.TaskListener {

    int fragVal;
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    private RecyclerView mRecyclerView;
    private PracticeAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    User session;
    String tempkey;
    String userID;
    String hasSelectedPractice;
    Functions functions;

    SwipeRefreshLayout mSwipeRefreshLayout;
    AsyncTaskPracticeList.TaskListener listener;
    ProgressBar progressBar;



    public static Step1Fragment init() {
        Step1Fragment truitonFrag = new Step1Fragment();
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;

        functions = new Functions (getActivity().getApplicationContext());
        session = new User(getActivity().getApplicationContext());
        tempkey = session.getTempKey();
        userID = session.getUid();

        //Check for previous Selected Clinic, skip step
        hasSelectedPractice = session.getCurrentPractice();

        if (hasSelectedPractice == null || hasSelectedPractice.isEmpty()) {
            loadPractices();
        } else {
            nextFragment();
            loadPractices();
        }

    }

    public void loadPractices(){
        //Load Asynchronously PRACTICE LIST
        listener = new AsyncTaskPracticeList.TaskListener() {
            @Override
            public void onFinished(ArrayList<Practice> results) {

                if (results.isEmpty() || results == null || results.size() < 1){

                    CustomAlertBox pDialog =
                            new CustomAlertBox(
                                    getActivity(),
                                    R.layout.dialogbox_nopractices,
                                    PracticeAddActivity.class);
                    pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pDialog.setCancelable(false);
                    pDialog.show();

                } else {

                    Log.d("PRACTCFISH", results.toString());

                    mAdapter = new PracticeAdapter(results, new PracticeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Practice item) {
                            //Save Selected clinic id to Keep Session
                            session.set(CommonKeys.KEY_PRACTICE_ID, item.getId());
                            nextFragment();
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        getPracticesTask();
    }

    public void nextFragment() {
        ((AppointmentsAddActivity)getActivity()).getViewPager().setCurrentItem(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layoutView = inflater.inflate(R.layout.appointments_add_step1_fragment, container,
                false);
        progressBar = (ProgressBar) layoutView.findViewById(R.id.progressBar);
        //Button nextButtonStep = (Button) layoutView.findViewById(R.id.nextButtonStep);

        /*nextButtonStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSave();
            }
        });*/

        mRecyclerView = (RecyclerView) layoutView.findViewById(R.id.recyclerView);
        //mRecyclerView.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getPracticesTask();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.okidoc_green,
                R.color.okidoc_green_darker);

        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        //nextButtonStep.setTypeface(fontRegular);

        return layoutView;

    }

    private void getPracticesTask() {
        if (functions.isNetworkAvailable()) {
            AsyncTaskPracticeList getPractices = new AsyncTaskPracticeList(getActivity(), listener);
            getPractices.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    private List<NameValuePair> collectForm () {

        String userID = session.getUid();

        params.add(new BasicNameValuePair("user_id", userID));
        params.add(new BasicNameValuePair("id_doctor", userID));
        params.add(new BasicNameValuePair("role", session.getRole()));
        params.add(new BasicNameValuePair("form", "appointment"));
        params.add(new BasicNameValuePair("url", "appointment.add.step1"));

        params.add(new BasicNameValuePair("tempkey", tempkey));

        return params;
    }

    private void runSave() {
        if (functions.isNetworkAvailable()) {
            //Get Data
        collectForm();

            AsyncTaskTempSaveAdd.TaskListener listener = new AsyncTaskTempSaveAdd.TaskListener() {
                @Override
                public void onFinished(String result) {
                    if (result.equals("1")) {
                        nextFragment();
                    }
                }
            };
            //Save Asynchronously
            AsyncTaskTempSaveAdd saveStep = new AsyncTaskTempSaveAdd(getActivity(),"appointments","2", params, listener, tempkey);
            saveStep.execute();

        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinished(ArrayList<Practice> result) {
    }

    @Override
    public void onFinished(String result) {
    }

}