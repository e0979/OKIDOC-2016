package com.colmenadeideas.okidoc.practices;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.appointments.AppointmentsListFragment;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.AsyncTaskPracticeList;
import com.colmenadeideas.okidoc.models.Practice;
import com.colmenadeideas.okidoc.models.adapters.PracticeAdapter;

import java.util.ArrayList;

public class PracticeListFragment extends Fragment
            implements  PracticeAdapter.OnItemClickListener,
                        AsyncTaskPracticeList.TaskListener {

    private RecyclerView mRecyclerView;
    private PracticeAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    ArrayList<Practice> mResultsData;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AsyncTaskPracticeList.TaskListener listener;

    User session;
    Functions functions;

    private boolean dosPaneles;

    public PracticeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = savedInstanceState != null ? savedInstanceState : getActivity().getIntent().getExtras();

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView;

        // No Practices (Always inflate this as default, then update for Other)
        rootView = inflater.inflate(R.layout.practice_list_empty_fragment, container, false);

        Button addPracticeButton = (Button) rootView.findViewById(R.id.add_practice_button);
        addPracticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PracticeAddActivity.class);
                startActivity(intent);
            }
        });


        //Load Asynchronously
        listener = new AsyncTaskPracticeList.TaskListener() {

            @Override
            public void onFinished(ArrayList<Practice> results) {
                Log.d("PRACTICES TOTAL:", Integer.toString(results.size()));
                mResultsData = results;

                if (results.isEmpty() || results == null || results.size() < 1) {
                   // Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                } else {

                    LinearLayout baseLayoutArea = (LinearLayout) rootView.findViewById(R.id.baseContainer);
                    View updateLayout = inflater.inflate(R.layout.practice_list_fragment, baseLayoutArea, false);
                    RelativeLayout noContent = (RelativeLayout) rootView.findViewById(R.id.noContent);
                    baseLayoutArea.removeView(noContent);
                    baseLayoutArea.addView(updateLayout); //Load Recycler area


                    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                    layoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(layoutManager);

                    mAdapter = new PracticeAdapter(results, new PracticeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Practice item) {
                           //Toast.makeText(getContext(), "Próximamente podrás editar esta información", Toast.LENGTH_LONG).show();

                            String practice_id = item.getId();

                            if (rootView.findViewById(R.id.detail_container) != null) {

                                Bundle arguments = new Bundle();
                                arguments.putString(PracticeEditDetailFragment.PRACTICE_ID, practice_id);
                                PracticeEditDetailFragment fragment = new PracticeEditDetailFragment();
                                fragment.setArguments(arguments);
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.detail_container, fragment)
                                        .commit();

                            } else {
                                //Start Activity EDIT

                                Intent intent = new Intent(getActivity(), PracticeEditDetailActivity.class);
                                intent.putExtra("practice_id", practice_id);
                                startActivity(intent);
                            }
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);



                    FloatingActionButton addPracticeButton = (FloatingActionButton) rootView.findViewById(R.id.add_practice);
                    addPracticeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Start Activity ADD
                            Intent intent = new Intent(getActivity(), PracticeAddActivity.class);
                            startActivity(intent);
                        }
                    });

                    mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
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

                }

            }

        };
        functions = new Functions(getActivity().getApplicationContext());
        session = new User(getActivity().getApplicationContext());
        getPracticesTask();


        return rootView;
    }

    public void getPracticesTask() {
        if (functions.isNetworkAvailable()) {
            AsyncTaskPracticeList getPractices = new AsyncTaskPracticeList(getActivity(), listener);
            getPractices.execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
            //mSwipeRefreshLayout.setRefreshing(false);
        }
    }




    @Override
    public void onFinished(ArrayList<Practice> result) {

    }

    @Override
    public void onItemClick(Practice item) {

    }


}