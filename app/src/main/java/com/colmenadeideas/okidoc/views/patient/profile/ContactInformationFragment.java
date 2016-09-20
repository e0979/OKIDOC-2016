
package com.colmenadeideas.okidoc.views.patient.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.libs.User;

public class ContactInformationFragment extends Fragment {

    int fragVal;
    User session;
    String tempkey;


    public static ContactInformationFragment init() {
        ContactInformationFragment truitonFrag = new ContactInformationFragment();
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
        session = new User(getActivity().getApplicationContext());
        tempkey = session.getTempKey();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layoutView = inflater.inflate(R.layout.patient_contactinfo_fragment, container,
                false);
        //Font
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_LIGHT);
        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_REGULAR);
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), CommonKeys.FONT_BOLD);

        return layoutView;

    }
}