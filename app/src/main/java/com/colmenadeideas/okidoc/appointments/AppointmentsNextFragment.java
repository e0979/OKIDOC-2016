package com.colmenadeideas.okidoc.appointments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.Appointment;
import com.colmenadeideas.okidoc.models.AsyncTaskNextAppointments;
import com.colmenadeideas.okidoc.models.NextAppointmentsResults;
import com.colmenadeideas.okidoc.models.adapters.AppointmentsDateAdapter;
import com.colmenadeideas.okidoc.patients.PatientAddActivity;
import com.colmenadeideas.okidoc.practices.PracticeAddActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AppointmentsNextFragment extends Fragment implements AsyncTaskNextAppointments.TaskListener {

    User session;
    String doctorId;
    ArrayList<Appointment> mResultsData;
    JSONArray jsonResults;

    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, ArrayList<Appointment>> listDataChild;

    SwipeRefreshLayout mSwipeRefreshLayout;
    AsyncTaskNextAppointments.TaskListener listener;

    Functions functions;

    public AppointmentsNextFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new User(getActivity());
        session.checkSession("AppointmentsNextFragment");

        doctorId = session.getUid();

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;

        rootView = inflater.inflate(R.layout.appointments_next_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getAppointmentsTasks();
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

        /*//FAB ACTIONS
        final FloatingActionButton addPracticeButton = (FloatingActionButton) rootView.findViewById(R.id.add_practice);
        addPracticeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PracticeAddActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton addPatientButton = (FloatingActionButton) rootView.findViewById(R.id.add_patient);
        addPatientButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PatientAddActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton addAppointmentButton = (FloatingActionButton) rootView.findViewById(R.id.add_appointment);
        addAppointmentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AppointmentsAddActivity.class);
                startActivity(intent);
            }
        });*/
        activateButtons(rootView);

        //Load Asynchronously
        listener = new AsyncTaskNextAppointments.TaskListener() {

            @Override
            public void onFinished(ArrayList<NextAppointmentsResults> results) {

                if (results.isEmpty() ||  results == null || results.size() < 1) {
                    //Toast.makeText(getActivity(), "No results", Toast.LENGTH_SHORT).show();

                    LinearLayout baseLayoutArea = (LinearLayout) rootView.findViewById(R.id.baseContainer);
                    View updateLayout = inflater.inflate(R.layout.appointments_empty_fragment, baseLayoutArea, false);
                    RelativeLayout noContent = (RelativeLayout) rootView.findViewById(R.id.noContent);
                    baseLayoutArea.removeView(noContent);
                    baseLayoutArea.addView(updateLayout);

                    final Button addAppointmentButton = (Button) rootView.findViewById(R.id.add_practice_button);
                    addAppointmentButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), AppointmentsAddActivity.class);
                            startActivity(intent);
                        }
                    });
                    final Button refreshButton = (Button) rootView.findViewById(R.id.refreshButton);
                    refreshButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Fragment fragment = new AppointmentsNextFragment();
                            Bundle args = new Bundle();
                            args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);

                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.mainArea, fragment)
                                    .commit();
                                    */

                            Fragment frg = null;
                            frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.mainArea);
                            final FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();

                        }
                    });

                    activateButtons(rootView);


                } else {
                    //has results, update
                    updateResults(results);

                }
            }
        };
        functions = new Functions(getActivity().getApplicationContext());

        session = new User(getActivity().getApplicationContext());
        getAppointmentsTasks();


        return rootView;
    }

    private void activateButtons(View rootView) {
        //FAB ACTIONS
        final FloatingActionButton addPracticeButton = (FloatingActionButton) rootView.findViewById(R.id.add_practice);
        addPracticeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PracticeAddActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton addPatientButton = (FloatingActionButton) rootView.findViewById(R.id.add_patient);
        addPatientButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PatientAddActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton addAppointmentButton = (FloatingActionButton) rootView.findViewById(R.id.add_appointment);
        addAppointmentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AppointmentsAddActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getAppointmentsTasks() {

        if (functions.isNetworkAvailable()) {

            AsyncTaskNextAppointments getNextAppointments = new AsyncTaskNextAppointments(getActivity(), listener, session.getUid());
            getNextAppointments.execute();

        } else {
            Toast.makeText(getActivity(), R.string.no_conection_msg, Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onFinished(ArrayList<NextAppointmentsResults> result) {
    }



    private void updateResults(ArrayList<NextAppointmentsResults> results) {

        listDataHeader = results.get(0).getListDataHeader();
        listDataChild = results.get(0).getListChild();

        expListView = (ExpandableListView) getActivity().findViewById(R.id.appointments_dates_list);
        AppointmentsDateAdapter listAdapter = new AppointmentsDateAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        //To Expand all children
        int count = listAdapter.getGroupCount();
        for (int position = 1; position <= count; position++) {
            expListView.expandGroup(position - 1);
        }
        //PREVENT COLLAPSING
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                /* String appointmentsItemCode =  listDataChild.get(
                       listDataHeader.get(groupPosition)).get(childPosition).getAppointmentCode();
                */
                String practiceCode =  listDataChild.get(
                        listDataHeader.get(groupPosition)).get(childPosition).getPracticeCode();

                //System.out.println("Choosen Date = : " + listDataHeader.get(groupPosition));
                Intent intent = new Intent(getActivity(), AppointmentsListActivity.class); //TODO "inflate" as Fragment, not as activity, so it can mantain menu sides
                Bundle b = new Bundle();
                b.putString("date", listDataHeader.get(groupPosition));
                b.putString("practice", practiceCode);
                //Add the bundle to the intent
                intent.putExtras(b);
                startActivity(intent);

                return false;
            }
        });

    }



}