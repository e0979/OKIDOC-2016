package com.colmenadeideas.okidoc.patients;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.Space;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colmenadeideas.okidoc.ModeloArticulos;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.models.AsyncTaskPatient;
import com.colmenadeideas.okidoc.models.PatientSimple;
import com.colmenadeideas.okidoc.views.patient.profile.ContactInformationFragment;
import com.colmenadeideas.okidoc.views.patient.profile.IndicationsFragment;
import com.colmenadeideas.okidoc.views.patient.profile.TimelineFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientDetailFragment  extends Fragment
        implements AppBarLayout.OnOffsetChangedListener {



    public static final String PATIENT_ID = "patient_id";
    String patient_id;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.5f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private Space fakeToolBar;
    FrameLayout nameSmallLayout;

    TextView fullName;
    TextView fullNameSmall;
    RelativeLayout ageLayout;    RelativeLayout heightLayout;    RelativeLayout weightLayout;    RelativeLayout bloodTypeLayout;
    RelativeLayout genderLayout;

    TextView ageField;    TextView heightField;    TextView weightField;    TextView bloodTypeField;
    TextView genderField;

    TabLayout tabLayout;

    //Tabs
    private ViewPager mViewPager;
    private PatientProfileAdapter mPagerAdapter;
    static final int ITEMS = 3;


    Functions functions;
    User session;
    Context mContext;

    // Articulo al cuál está ligado la UI
    private ModeloArticulos.Articulo itemDetallado;

    public PatientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        functions = new Functions(mContext);
        session = new User(mContext);
        if (getArguments().containsKey(PATIENT_ID)) {
            patient_id = getArguments().getString(PATIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.patient_detail_fragment, container, false);

        /*if (itemDetallado != null) {
            // Toolbar en master-detail
            Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_detalle);
            if (toolbar != null)
                toolbar.inflateMenu(R.menu.menu_detalle_articulo);

            ((TextView) v.findViewById(R.id.titulo)).setText(itemDetallado.titulo);
            ((TextView) v.findViewById(R.id.fecha)).setText(itemDetallado.fecha);
            ((TextView) v.findViewById(R.id.contenido)).setText(getText(R.string.lorem));
            Glide.with(this)
                    .load(itemDetallado.urlMiniatura)
                    .into((ImageView) v.findViewById(R.id.imagen));
        }*/


        mToolbar        = (Toolbar) layoutView.findViewById(R.id.main_toolbar);
        mTitle          = (TextView) layoutView.findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) layoutView.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) layoutView.findViewById(R.id.main_appbar);
        fakeToolBar     = (Space) layoutView.findViewById(R.id.fake_toolbar);

        nameSmallLayout = (FrameLayout) layoutView.findViewById(R.id.nameSmall);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nameSmallLayout.getLayoutParams();
        lp.height = 0;

        fullName        = (TextView) layoutView.findViewById(R.id.fullName);
        fullNameSmall   = (TextView) layoutView.findViewById(R.id.fullNameSmall);



        genderLayout   =  (RelativeLayout) layoutView.findViewById(R.id.layout_age);
        ageLayout   =  (RelativeLayout) layoutView.findViewById(R.id.layout_age);
        heightLayout   =  (RelativeLayout) layoutView.findViewById(R.id.layout_height);
        weightLayout   =  (RelativeLayout) layoutView.findViewById(R.id.layout_weight);
        bloodTypeLayout   =  (RelativeLayout) layoutView.findViewById(R.id.layout_bloodtype);

        genderField    =  (TextView) layoutView.findViewById(R.id.gender);
        ageField    =  (TextView) layoutView.findViewById(R.id.age);
        heightField =  (TextView) layoutView.findViewById(R.id.height);
        weightField =  (TextView) layoutView.findViewById(R.id.weight);
        bloodTypeField =  (TextView) layoutView.findViewById(R.id.bloodtype);

        loadPatient();


        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.patient_detail_menu);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        //Tabs
        mPagerAdapter = new PatientProfileAdapter(getFragmentManager());
        mViewPager = (ViewPager) layoutView.findViewById(R.id.tabsViewPager);
        mViewPager.setAdapter(mPagerAdapter);

        tabLayout = (TabLayout) layoutView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        return layoutView;
    }

    private void loadPatient(){
        //Load Asynchronously
        if (functions.isNetworkAvailable()) {
            AsyncTaskPatient.TaskListener listener = new AsyncTaskPatient.TaskListener() {

                @Override
                public void onFinished(ArrayList<PatientSimple> results) {

                    if (results.isEmpty() || results == null || results.size() < 1) {
                         Toast.makeText(getActivity(), "An error ocurred fetching data!", Toast.LENGTH_SHORT).show();
                    } else {
                        fillDatainView(results);
                    }
                }
            };

            AsyncTaskPatient getPatient = new AsyncTaskPatient(mContext, listener, patient_id);
            getPatient.execute();

        } else {
            Toast.makeText(mContext, R.string.no_conection_msg, Toast.LENGTH_LONG).show();
        }
    }

    private void fillDatainView(ArrayList<PatientSimple> results) {

        PatientSimple patient = results.get(0);
        String FullName = patient.getName() +" " +patient.getLastname();
        fullName.setText(FullName);
        fullNameSmall.setText(FullName);
        mTitle.setText(FullName);

        JSONObject patientData = patient.getData();
        try {
            String gender = patientData.getString("gender");
            String age = patientData.getString("age");
            String height = patientData.getString("height");
            String weight = patientData.getString("weight");
            String bloodType = patientData.getString("blood_type");

            genderField.setText(patientData.getString("gender"));
            ageField.setText(patientData.getString("age")+" años");
            heightField.setText(patientData.getString("height")+" CMS");
            weightField.setText(patientData.getString("weight")+" KG");
            bloodTypeField.setText(patientData.getString("blood_type"));

            if (gender == "null") {
                genderLayout.setVisibility(View.GONE);
            }
            if (age == "null") {
                ageLayout.setVisibility(View.GONE);
            }
            if (height == "null") {
                heightLayout.setVisibility(View.GONE);
            }
            if (weight == "null") {
                weightLayout.setVisibility(View.GONE);
            }
            if (bloodType == "null") {
                bloodTypeLayout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //age.setText(patient.get);
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
                //startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                fakeToolBar.setVisibility(View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                //startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                fakeToolBar.setVisibility(View.GONE);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nameSmallLayout.getLayoutParams();
                lp.height = 0;
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nameSmallLayout.getLayoutParams();
                lp.height = 75;
                fakeToolBar.setVisibility(View.GONE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nameSmallLayout.getLayoutParams();
                lp.height = 0;

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
                case 0:
                    return ContactInformationFragment.init();
                //case 1:
                    //return IndicationsFragment.init();
                default:
                    return TimelineFragment.init();
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Datos Personales";
                case 1:
                    return "Línea de vida";
            }
            return null;
        }
    }


    public ViewPager getViewPager() {
        if (null == mViewPager) {
            mViewPager = (ViewPager) getView().findViewById(R.id.tabsViewPager);
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
