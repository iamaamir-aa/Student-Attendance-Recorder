package com.android.studentattendancerecorder.Model;

import java.util.ArrayList;
import java.util.HashMap;


public class PdfDataParticulars {
    private String class_name,subject_name,month,school,classStarted,teacher,sem,strength;
    private ArrayList<HashMap<String,ArrayList<HashMap<Integer , Character>>>> listOfStudentAndAttendance;

    public PdfDataParticulars(String class_name, String subject_name, String month, String school, String classStarted, String teacher, String sem, String strength, ArrayList<HashMap<String, ArrayList<HashMap<Integer, Character>>>> listOfStudentAndAttendance) {
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.month = month;
        this.school = school;
        this.classStarted = classStarted;
        this.teacher = teacher;
        this.sem = sem;
        this.strength = strength;
        this.listOfStudentAndAttendance = listOfStudentAndAttendance;
    }

    @Override
    public String toString() {
        return "PdfDataParticulars{" +
                "class_name='" + class_name + '\'' +
                ", subject_name='" + subject_name + '\'' +
                ", month='" + month + '\'' +
                ", school='" + school + '\'' +
                ", classStarted='" + classStarted + '\'' +
                ", teacher='" + teacher + '\'' +
                ", sem='" + sem + '\'' +
                ", strength='" + strength + '\'' +
                ", listOfStudentAndAttendance=" + listOfStudentAndAttendance +
                '}';
    }

    public PdfDataParticulars() {
        listOfStudentAndAttendance=new ArrayList<>();
    }

    public ArrayList<HashMap<String, ArrayList<HashMap<Integer, Character>>>> getListOfStudentAndAttendance() {
        return listOfStudentAndAttendance;
    }

    public void setListOfStudentAndAttendance(ArrayList<HashMap<String, ArrayList<HashMap<Integer, Character>>>> listOfStudentAndAttendance) {
        this.listOfStudentAndAttendance = listOfStudentAndAttendance;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassStarted() {
        return classStarted;
    }

    public void setClassStarted(String classStarted) {
        this.classStarted = classStarted;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

}
