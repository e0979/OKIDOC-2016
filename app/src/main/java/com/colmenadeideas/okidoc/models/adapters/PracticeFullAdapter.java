package com.colmenadeideas.okidoc.models.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.models.Appointment;
import com.colmenadeideas.okidoc.models.PatientSimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class PracticeFullAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listOfDatesHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Appointment>> listDataChild;

    Typeface fontLight;
    Typeface fontRegular;
    Typeface fontBold;

    public PracticeFullAdapter(Context context, List<String> listOfDatesHeader,
                                   HashMap<String, ArrayList<Appointment>> listDataChild) {
        this.context = context;
        this.listOfDatesHeader = listOfDatesHeader;
        this.listDataChild = listDataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listOfDatesHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflaInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaInflater.inflate(R.layout.appointments_date_item, null);
        }
        //Font
        fontLight =  Typeface.createFromAsset(context.getAssets(), CommonKeys.FONT_LIGHT);
        fontRegular =  Typeface.createFromAsset(context.getAssets(),CommonKeys.FONT_REGULAR);
        fontBold =  Typeface.createFromAsset(context.getAssets(), CommonKeys.FONT_BOLD);

        Appointment appointmentsItem = (Appointment) getChild(groupPosition, childPosition);

        TextView practice = (TextView) convertView.findViewById(R.id.PracticeNameText);


        TextView totalAppointments = (TextView) convertView.findViewById(R.id.patients_count);

        if (appointmentsItem.getTotalAppointments() < 3) {
            totalAppointments.setVisibility(View.INVISIBLE);

        } else {
            int extraAppointments = appointmentsItem.getTotalAppointments()-3;
            totalAppointments.setText(extraAppointments);
        }


        final String thy = appointmentsItem.getAppointmentCode().toString();
        //totalAppointments.setText( getChild(groupPosition, childPosition).toString());

            /* if I would want items to have individual click listeners
            totalAppointments.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {

                        System.out.println("Clicky " + thy);
                        //Intent i = new Intent(Intent.ACTION_VIEW, uriToOpen);
                        //startActivity(i);
                    }
             });
            */


        practice.setText(appointmentsItem.getPracticeName().trim());
        ArrayList<PatientSimple> patientsData = appointmentsItem.getPatients();

        final int max = patientsData.size();

       /* for (int a =0; a < max; a++ ) {
            String resourceName = "name_"+a;
            int resourceInt = context.getResources().getIdentifier(resourceName, "id", context.getPackageName());
            TextView patient_new = (TextView) convertView.findViewById(resourceInt);
            //Name
            //patient_new.setText(patientsData.get(a).get(1).toString());
            patient_new.setText("Paciente Name");
            patient_new.setTypeface(fontRegular);
        }



        practice.setTypeface(fontBold);*/

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listOfDatesHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listOfDatesHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listOfDatesHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String fullDate = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflaInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaInflater.inflate(R.layout.appointments_date_block, null);
        }



        //Font
        fontLight =  Typeface.createFromAsset(context.getAssets(), CommonKeys.FONT_LIGHT);
        fontRegular =  Typeface.createFromAsset(context.getAssets(),CommonKeys.FONT_REGULAR);
        fontBold =  Typeface.createFromAsset(context.getAssets(), CommonKeys.FONT_BOLD);

        TextView dateDay = (TextView) convertView.findViewById(R.id.date_day);
        TextView dateMonth = (TextView) convertView.findViewById(R.id.date_month);


        String[] datePieces = fullDate.split("-");
        String dateYear = datePieces[0].substring(datePieces[0].length() - 2);

        dateDay.setTypeface(fontBold);
        dateMonth.setText(datePieces[1]+"/"+dateYear);
        dateDay.setText(datePieces[2]);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
