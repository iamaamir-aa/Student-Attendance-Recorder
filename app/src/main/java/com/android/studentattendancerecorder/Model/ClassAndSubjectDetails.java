package com.android.studentattendancerecorder.Model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ClassAndSubjectDetails {
    private String class_name;
    private String subject_name;
    private String id;
    private ArrayList<StudentsDetail> students;

    public ClassAndSubjectDetails(String class_name, String subject_name, String id, ArrayList<StudentsDetail> students) {
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.id = id;
        this.students = students;
    }

    public ArrayList<StudentsDetail> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentsDetail> students) {
        this.students = students;
    }

    public ClassAndSubjectDetails(String aClass, String subject) {
        class_name = aClass;
        subject_name = subject;
    }

    public ClassAndSubjectDetails(String class_name, String subject_name, String id) {
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
