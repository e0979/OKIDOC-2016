package com.colmenadeideas.okidoc.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NextAppointmentsResults {

    ArrayList<Appointment> appointments;
    HashMap<String, ArrayList<Appointment>> listChild;
    List<String> listDataHeader;

    public NextAppointmentsResults(ArrayList<Appointment> appointments,
                                   HashMap<String, ArrayList<Appointment>> listChild,
                                   List<String> listDataHeader){

        super();
        this.appointments   = appointments;
        this.listChild      = listChild;
        this.listDataHeader = listDataHeader;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }
    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public HashMap<String, ArrayList<Appointment>> getListChild() {
        return listChild;
    }
    public void setListChild(HashMap<String, ArrayList<Appointment>> listChild) {
        this.listChild = listChild;
    }

    public List<String> getListDataHeader() {
        return listDataHeader;
    }
    public void setListDataHeader(List<String> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }
}
