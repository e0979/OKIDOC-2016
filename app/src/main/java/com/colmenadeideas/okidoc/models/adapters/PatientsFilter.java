package com.colmenadeideas.okidoc.models.adapters;


import android.widget.Filter;
import com.colmenadeideas.okidoc.models.PatientSimple;
import java.util.ArrayList;
import java.util.List;

class PatientsFilter extends Filter {

    PatientsAdapter adapter;
    ArrayList<PatientSimple> originalList;
    ArrayList<PatientSimple> filteredList;
    String showWhat;

    public PatientsFilter(PatientsAdapter adapter, ArrayList<PatientSimple> originalList, String showWhat) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
        this.showWhat = showWhat;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (final PatientSimple patient : originalList) {

                switch (showWhat) {
                    case "email":
                        if (patient.email.toLowerCase().contains(filterPattern)) {
                            filteredList.add(patient);
                        }
                        break;
                    case "lastname":
                        if (patient.lastname.toLowerCase().contains(filterPattern)) {
                            filteredList.add(patient);
                        }
                        break;
                    default:
                        if (patient.name.toLowerCase().contains(filterPattern)) {
                            filteredList.add(patient);
                        }
                        break;
                }
            }

        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.count == 0) {
            adapter.notifyDataSetInvalidated();
        } else {
           // planetList = (List<Planet>) results.values;
            //notifyDataSetChanged();
            adapter.filteredPatients.clear();
            adapter.filteredPatients.addAll((List) results.values);
            adapter.notifyDataSetChanged();
        }

    }
}
