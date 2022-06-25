package com.android.studentattendancerecorder.Model;

public class StudentsDetail {
    private String studentName;
    private String enrollmentNumber;
    private Boolean present;

    public StudentsDetail(String studentName, String enrollmentNumber, Boolean present) {
        this.studentName = studentName;
        this.enrollmentNumber = enrollmentNumber;
        this.present = present;
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

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }
}
