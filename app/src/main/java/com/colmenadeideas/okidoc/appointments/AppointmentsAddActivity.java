package com.colmenadeideas.okidoc.appointments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.views.appointment.add.Step1Fragment;
import com.colmenadeideas.okidoc.views.appointment.add.Step2Fragment;
import com.colmenadeideas.okidoc.views.appointment.add.Step4Fragment;
import com.colmenadeideas.okidoc.views.appointment.add.Step5Fragment;
import com.colmenadeideas.okidoc.views.appointment.add.StepPreviewFragment;


    public class AppointmentsAddActivity extends AppCompatActivity {

        private ViewPager viewPager;
        private PatientAddAdapter mPagerAdapter;
        static final int ITEMS = 5;
        private int currentPage;
        PageListener pageListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //Store 'Temp Key' for these
            User session = new User(this);
            String tempKey = Functions.generateTempKey(session.getEmail());
            session.set(CommonKeys.KEY_TEMP_FORM_KEY, tempKey);
            //End store 'Temp Key'


            setContentView(R.layout.appointments_add_form_steps);

            viewPager = (ViewPager) findViewById(R.id.stepsViewPager);
            mPagerAdapter = new PatientAddAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mPagerAdapter);

            pageListener = new PageListener();
            viewPager.setOnPageChangeListener(pageListener);

            TextView title = (TextView) findViewById(R.id.title);
            //Font
            Typeface fontLight = Typeface.createFromAsset(getAssets(), CommonKeys.FONT_LIGHT);
            Typeface fontRegular = Typeface.createFromAsset(getAssets(), CommonKeys.FONT_REGULAR);
            Typeface fontBold = Typeface.createFromAsset(getAssets(), CommonKeys.FONT_BOLD);

            title.setTypeface(fontLight);


        }





        private class PageListener extends ViewPager.SimpleOnPageChangeListener {
            public void onPageSelected(int position) {

                if (position == 1) { //Page 2, get Dates based on

                }
                Log.i("TAGI", "page selected " + position);
            }
        }


        public static class PatientAddAdapter extends FragmentStatePagerAdapter {
            public PatientAddAdapter(FragmentManager fragmentManager) {
                super(fragmentManager);
            }

            @Override
            public int getCount() {
                return ITEMS;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: // Choose Clinic
                        return Step1Fragment.init();
                    case 1: // Choose Date
                        return Step2Fragment.init();
                    case 2: // Register Patient
                        return Step4Fragment.init();
                    case 3: // Register Patient history
                        return Step5Fragment.init();
                    default: //Preview
                        return StepPreviewFragment.init();
                    //return PracticeAddStepLISTFragment.init(position);
                }
            }
        }

        public ViewPager getViewPager() {
            if (null == viewPager) {
                viewPager = (ViewPager) findViewById(R.id.stepsViewPager);
            }
            return viewPager;
        }

    }