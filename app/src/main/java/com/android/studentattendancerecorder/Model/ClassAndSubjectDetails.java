package com.android.studentattendancerecorder.Model;

import androidx.annotation.NonNull;

public class ClassAndSubjectDetails {
    private String class_name;
    private String subject_name;
    private String id;

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
