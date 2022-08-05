package com.android.studentattendancerecorder.Model;

import java.util.ArrayList;

public class StudentsDetail {
    private String studentName;
    private String enrollmentNumber;
    private String id;
    private ArrayList<Dates> date;

    public StudentsDetail(String studentName, String enrollmentNumber, String id, ArrayList<Dates> dates) {
        this.studentName = studentName;
        this.enrollmentNumber = enrollmentNumber;
        this.id = id;
        this.date = dates;
    }

    public ArrayList<Dates> getDates() {
        return date;
    }

    public void setDates(ArrayList<Dates> dates) {
        this.date = dates;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    public void setEnrollmentNumber(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber;
    }

}
