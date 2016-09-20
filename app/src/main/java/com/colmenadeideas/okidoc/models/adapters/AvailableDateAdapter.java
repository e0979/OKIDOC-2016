package com.colmenadeideas.okidoc.models.adapters;



import com.colmenadeideas.okidoc.models.AvailableDate;

import java.util.ArrayList;

public class AvailableDateAdapter {

    private final ArrayList<AvailableDate> listAvailableDates;

    public AvailableDateAdapter(ArrayList<AvailableDate> listAvailableDates/*, OnItemClickListener listener*/) {
        this.listAvailableDates = listAvailableDates;
    }


    /*@Override
    public int getItemCount() {
        if (listAvailableDates != null) {
            return listAvailableDates.size() > 0 ? listAvailableDates.size() : 0;
        } else {
            return 0;
        }
    }*/

}