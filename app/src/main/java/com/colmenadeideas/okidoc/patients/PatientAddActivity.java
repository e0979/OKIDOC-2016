package com.colmenadeideas.okidoc.patients;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.Functions;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.views.patient.add.Step1Fragment;
import com.colmenadeideas.okidoc.views.patient.add.Step2Fragment;
import com.colmenadeideas.okidoc.views.practice.add.Step3Fragment;
import com.colmenadeideas.okidoc.views.practice.add.StepPreviewFragment;


import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.colmenadeideas.okidoc.views.practice.add.PracticeAddStepLISTFragment;

import org.json.JSONObject;


public class PatientAddActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PatientAddAdapter mPagerAdapter;
    static final int ITEMS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Store 'Temp Key' for these
        User session = new User(this);
        String tempKey = Functions.generateTempKey(session.getEmail());
        session.set(CommonKeys.KEY_TEMP_FORM_KEY, tempKey);
        //End store 'Temp Key'


        setContentView(R.layout.patient_add_form_steps);


        viewPager = (ViewPager) findViewById(R.id.stepsViewPager);
        mPagerAdapter = new PatientAddAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        TextView title = (TextView) findViewById(R.id.title);
        //Font
        Typeface fontLight = Typeface.createFromAsset(getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), CommonKeys.FONT_BOLD);

        title.setTypeface(fontLight);


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
                case 0: // Fragment # 1
                    return Step1Fragment.init(position);
                case 1: // Fragment # 2
                    return Step2Fragment.init(position);
                case 2: // Fragment # 3
                    return Step3Fragment.init();
               /* case 3: // Fragment # 4
                    return Step4Fragment.init(position);*/
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