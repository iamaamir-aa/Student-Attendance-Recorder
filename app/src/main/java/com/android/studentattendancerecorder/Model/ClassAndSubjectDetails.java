package com.android.studentattendancerecorder.Model;

import androidx.annotation.NonNull;

public class ClassAndSubjectDetails {
    private String class_name;
    private String subject_name;
    static int id=0;
    private StudentsDetail studentsDetail;

    public StudentsDetail getStudentsDetail() {
        return studentsDetail;
    }

    public void setStudentsDetail(StudentsDetail studentsDetail) {
        this.studentsDetail = studentsDetail;
    }

    public ClassAndSubjectDetails(String class_name, String subject_name, StudentsDetail studentsDetail) {
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.studentsDetail = studentsDetail;
    }

    public ClassAndSubjectDetails(String aClass, String subject) {
        class_name = aClass;
        subject_name = subject;
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

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        ClassAndSubjectDetails.id = id;
    }
}
