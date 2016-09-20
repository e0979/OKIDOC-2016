package com.colmenadeideas.okidoc.models.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.models.PatientSimple;

import java.util.ArrayList;

public class PatientsAdapter extends ArrayAdapter<PatientSimple> {

    Context mContext;
    int layoutResourceId;
    private final ArrayList<PatientSimple> patients;
    ArrayList<PatientSimple> filteredPatients = new ArrayList<>();
    String showWhat;

    public PatientsAdapter(Context mContext, int layoutResourceId, ArrayList<PatientSimple> patients, String showWhat) {
        super(mContext, layoutResourceId, patients);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.patients = patients;
        this.showWhat = showWhat;
    }

    @Override
    public int getCount() {
        return filteredPatients.size();
    }
    @Override
    public Filter getFilter() {
        return new PatientsFilter(this, patients, showWhat);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item from filtered list.
        PatientSimple patient = filteredPatients.get(position);
        // PatientSimple previousPosition  = patients.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.autocomplete_search_withimage, parent, false);

        TextView viewPatientId     = (TextView) convertView.findViewById(R.id.patient_id);
        TextView textviewName       = (TextView) convertView.findViewById(R.id.textArea);
        TextView viewHiddenName   = (TextView) convertView.findViewById(R.id.hidden_name);
        TextView viewHiddenLastName   = (TextView) convertView.findViewById(R.id.hidden_lastname);
        TextView viewHiddenEmail   = (TextView) convertView.findViewById(R.id.hidden_email);
        TextView viewHiddenIdcard   = (TextView) convertView.findViewById(R.id.hidden_id_card);
        TextView viewHiddenBirth   = (TextView) convertView.findViewById(R.id.hidden_birth);
        TextView viewHiddenPhone   = (TextView) convertView.findViewById(R.id.hidden_phone);


        viewPatientId.setText(patient.id);
        viewHiddenName.setText(patient.name);
        viewHiddenLastName.setText(patient.lastname);
        viewHiddenEmail.setText(patient.email);
        viewHiddenIdcard.setText(patient.id_card);
        viewHiddenBirth.setText(patient.birth);
        viewHiddenPhone.setText(patient.phone);


        //ivIcon.setImageResource(patient.drawable);
        switch (showWhat) {
            case "email":
                textviewName.setText(patient.email);
                break;
            case "lastname":
                textviewName.setText(patient.lastname + ", " + patient.name);
                break;
            default:
                textviewName.setText(patient.name + " " + patient.lastname);
                break;
        }

        return convertView;

    }
}