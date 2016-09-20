package com.colmenadeideas.okidoc.models;

import java.util.ArrayList;

/**
 * Created by dlarez on 26/12/14.
 */
public class Appointment {
    public String appointmentCode;
    public String practiceName;
    public String practiceCode;
    //private ArrayList<ArrayList<String>> patients;
    public ArrayList<PatientSimple> patients;
    public int totalAppointments;

    public Appointment(String appointmentCode,
                            String practiceName,
                            String practiceCode,
                            ArrayList<PatientSimple> patients,
                            int totalAppointments) {
        super();
        this.appointmentCode = appointmentCode;
        this.practiceName = practiceName;
        this.practiceCode = practiceCode;
        this.patients = patients;
        this.totalAppointments = totalAppointments;
    }

    public String getAppointmentCode() {
        return appointmentCode;
    }
    public void setAppointmentCode(String appointmentCode) {
        this.appointmentCode = appointmentCode;
    }
    public String getPracticeName() {
        return practiceName;
    }
    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    public String getPracticeCode(){
        return practiceCode;
    }

    public void setPracticeCode(String practiceCode) {
        this.practiceCode = practiceCode;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }
    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }


    public ArrayList<PatientSimple> getPatients() {
        return patients;
    }
    public void setPatients( ArrayList<PatientSimple> patients) {
        this.patients = patients;
    }

}
