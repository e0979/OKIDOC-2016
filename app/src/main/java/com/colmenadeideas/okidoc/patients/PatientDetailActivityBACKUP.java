    package com.colmenadeideas.okidoc.patients;


    import android.os.Bundle;
    import android.support.design.widget.AppBarLayout;
    import android.support.design.widget.TabLayout;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;
    import android.support.v4.app.FragmentStatePagerAdapter;
    import android.support.v4.view.ViewPager;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.animation.AlphaAnimation;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.colmenadeideas.okidoc.R;
    import com.colmenadeideas.okidoc.includes.config.libs.Functions;
    import com.colmenadeideas.okidoc.includes.config.libs.User;
    import com.colmenadeideas.okidoc.models.AsyncTaskPatient;
    import com.colmenadeideas.okidoc.models.PatientSimple;
    import com.colmenadeideas.okidoc.views.patient.profile.IndicationsFragment;
    import com.colmenadeideas.okidoc.views.patient.profile.TimelineFragment;

    import java.util.ArrayList;

    public class PatientDetailActivityBACKUP extends AppCompatActivity
            implements AppBarLayout.OnOffsetChangedListener {

        private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
        private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
        private static final int ALPHA_ANIMATIONS_DURATION              = 200;

        private boolean mIsTheTitleVisible          = false;
        private boolean mIsTheTitleContainerVisible = true;

        private LinearLayout mTitleContainer;
        private TextView mTitle;
        private AppBarLayout mAppBarLayout;
        private Toolbar mToolbar;

        TextView fullName;

        //Tabs
        private ViewPager mViewPager;
        private PatientProfileAdapter mPagerAdapter;
        static final int ITEMS = 2;
        private SectionsPagerAdapter mSectionsPagerAdapter;

        String patientId;

        Functions functions;
        User session;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.patient_detail_activity);


        /*Bundle bundle = savedInstanceState != null ? savedInstanceState : getActivity().getIntent().getExtras();
        String patientId2 = getActivity().getIntent().getExtras().getString("date");*/
            patientId = getIntent().getExtras().getString("patient_id");


            functions = new Functions(this);
            session = new User(this);



            bindActivity();
            loadPatient();

            mAppBarLayout.addOnOffsetChangedListener(this);

            mToolbar.inflateMenu(R.menu.patient_detail_menu);
            startAlphaAnimation(mTitle, 0, View.INVISIBLE);


            //Tabs
            mPagerAdapter = new PatientProfileAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.tabsViewPager);
            mViewPager.setAdapter(mPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);



        }
        private void loadPatient(){
            //Load Asynchronously
            if (functions.isNetworkAvailable()) {
                AsyncTaskPatient.TaskListener listener = new AsyncTaskPatient.TaskListener() {

                    @Override
                    public void onFinished(ArrayList<PatientSimple> results) {

                        Log.d("Patients Total:", Integer.toString(results.size()));

                        if (results.isEmpty() || results == null || results.size() < 1) {
                            // Toast.makeText(getActivity(), "patients, Failed to fetch data!", Toast.LENGTH_SHORT).show();
                        } else {


                            PatientSimple patient = results.get(0);
                            fullName.setText(patient.getName() +" " +patient.getLastname());
                            //Get Patients just once
                           /* PatientsAdapter adapter =
                                    new PatientsAdapter(PatientDetailActivity.this, R.layout.autocomplete_search_withimage, results, "");

                            nameAutocomplete.setAdapter(adapter);*/


                        }
                    }
                };
                session = new User(PatientDetailActivityBACKUP.this);
                AsyncTaskPatient getPatient = new AsyncTaskPatient(PatientDetailActivityBACKUP.this, listener, patientId);
                getPatient.execute();

            } else {
                Toast.makeText(PatientDetailActivityBACKUP.this, R.string.no_conection_msg, Toast.LENGTH_LONG).show();
            }
        }

        private void bindActivity() {
            mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
            mTitle          = (TextView) findViewById(R.id.main_textview_title);
            mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
            mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
            fullName = (TextView) findViewById(R.id.fullName);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.patient_detail_menu, menu);
            return true;
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
            int maxScroll = appBarLayout.getTotalScrollRange();
            float percentage = (float) Math.abs(offset) / (float) maxScroll;

            handleAlphaOnTitle(percentage);
            handleToolbarTitleVisibility(percentage);
        }

        private void handleToolbarTitleVisibility(float percentage) {
            if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

                if(!mIsTheTitleVisible) {
                    startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                    mIsTheTitleVisible = true;
                }

            } else {

                if (mIsTheTitleVisible) {
                    startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                    mIsTheTitleVisible = false;
                }
            }
        }

        private void handleAlphaOnTitle(float percentage) {
            if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
                if(mIsTheTitleContainerVisible) {
                    startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                    mIsTheTitleContainerVisible = false;
                }

            } else {

                if (!mIsTheTitleContainerVisible) {
                    startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                    mIsTheTitleContainerVisible = true;
                }
            }
        }

        public static void startAlphaAnimation (View v, long duration, int visibility) {
            AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                    ? new AlphaAnimation(0f, 1f)
                    : new AlphaAnimation(1f, 0f);

            alphaAnimation.setDuration(duration);
            alphaAnimation.setFillAfter(true);
            v.startAnimation(alphaAnimation);
        }

        public static class PatientProfileAdapter extends FragmentStatePagerAdapter {
            public PatientProfileAdapter(FragmentManager fragmentManager) {
                super(fragmentManager);
            }

            @Override
            public int getCount() {
                return ITEMS;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: // Fragment # 1
                        return TimelineFragment.init();
                    case 1: // Fragment # 2
                        return IndicationsFragment.init();
                    default: //Preview
                        return TimelineFragment.init();
                }
            }
        }

        public ViewPager getViewPager() {
            if (null == mViewPager) {
                mViewPager = (ViewPager) findViewById(R.id.tabsViewPager);
            }
            return mViewPager;
        }




        public static class PlaceholderFragment extends Fragment {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private static final String ARG_SECTION_NUMBER = "section_number";

            public PlaceholderFragment() {
            }

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            public static PlaceholderFragment newInstance(int sectionNumber) {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);

                return fragment;
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
        }
        public class SectionsPagerAdapter extends FragmentPagerAdapter {

            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                // getItem is called to instantiate the fragment for the given page.
                // Return a PlaceholderFragment (defined as a static inner class below).
                return PlaceholderFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                // Show 3 total pages.
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "SECTION 1";
                    case 1:
                        return "SECTION 2";
                    case 2:
                        return "SECTION 3";
                }
                return null;
            }
        }

    }

