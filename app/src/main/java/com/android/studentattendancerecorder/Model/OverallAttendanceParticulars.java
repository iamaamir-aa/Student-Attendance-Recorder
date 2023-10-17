package com.android.studentattendancerecorder.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class OverallAttendanceParticulars {
    private String departmentName,class_name,subject_name,month,classStarted,teacher,sem,strength;
    private ArrayList<HashMap<String,HashMap<String,Double>>> listOfStudentAndOverallAttendance;

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return "OverallAttendanceParticulars{" +
                "departmentName='" + departmentName + '\'' +
                ", class_name='" + class_name + '\'' +
                ", subject_name='" + subject_name + '\'' +
                ", month='" + month + '\'' +
                ", classStarted='" + classStarted + '\'' +
                ", teacher='" + teacher + '\'' +
                ", sem='" + sem + '\'' +
                ", strength='" + strength + '\'' +
                ", listOfStudentAndOverallAttendance=" + listOfStudentAndOverallAttendance +
                '}';
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public ArrayList<HashMap<String, HashMap<String, Double>>> getListOfStudentAndOverallAttendance() {
        return listOfStudentAndOverallAttendance;
    }

    public void setListOfStudentAndOverallAttendance(ArrayList<HashMap<String, HashMap<String, Double>>> listOfStudentAndOverallAttendance) {
        this.listOfStudentAndOverallAttendance = listOfStudentAndOverallAttendance;
    }
}
