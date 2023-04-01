package com.android.studentattendancerecorder;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.studentattendancerecorder.Model.Dates;
import com.android.studentattendancerecorder.Model.OverallAttendanceParticulars;
import com.android.studentattendancerecorder.Model.PdfDataParticulars;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class printAttendance extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    int year, monthInt, date;
    Button generatePdfButton;
    RadioGroup radioGroupSelectPrint;
    private Spinner dropdown;
    private String CLASS_ID;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private ArrayList<String> dropList;
    private String monthSelected;
    private int monthToPrint=0, yearToPrint=0;
    private boolean radioPressed=false;

    private void monthAttendance(int monthOfAttendance, int yearOfAttendance) {
        databaseRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PdfDataParticulars saveData = new PdfDataParticulars();
                String departmentName = snapshot.child("departmentName").getValue().toString();
                String teacherName = snapshot.child("name").getValue().toString();
                String class_name = snapshot.child("CLASS").child(CLASS_ID).child("class_name").getValue().toString();
                String subject_name = snapshot.child("CLASS").child(CLASS_ID).child("subject_name").getValue().toString();
                String strength = snapshot.child("CLASS").child(CLASS_ID).child("strength").getValue().toString();
                String classStarted = snapshot.child("CLASS").child(CLASS_ID).child("classStarted").getValue().toString();
                String semester = snapshot.child("CLASS").child(CLASS_ID).child("semester").getValue().toString();
                String monthString = new DateFormatSymbols().getMonths()[monthOfAttendance - 1];
                saveData.setDepartmentName(departmentName);
                saveData.setTeacher(teacherName);
                saveData.setClass_name(class_name);
                saveData.setSubject_name(subject_name);
                saveData.setStrength(strength);
                saveData.setClassStarted(classStarted);
                saveData.setMonth(monthString);
                saveData.setSem(semester);
                ArrayList<HashMap<String, HashMap<Integer, Character>>> listOfStudentAndAttendance = new ArrayList<>();

                for (DataSnapshot d : snapshot.child("CLASS").child(CLASS_ID).child("students").getChildren()) {
                    HashMap<String, HashMap<Integer, Character>> firstHash = new HashMap<>();
                    String keyStudent = d.getRef().getKey();
                    String studentName = d.child("studentName").getValue().toString();
                    System.out.println(studentName);
                    HashMap<Integer, Character> secondHash = new HashMap<>();
                    //for (DataSnapshot e : snapshot.child("CLASS").child(CLASS_ID).child("students").child(keyStudent).getChildren()) {
                    if (snapshot.child("CLASS").child(CLASS_ID).child("students").child(keyStudent).child("DATES").hasChild(String.valueOf(yearOfAttendance)) &&
                            snapshot.child("CLASS").child(CLASS_ID).child("students").child(keyStudent).child("DATES").child(String.valueOf(yearOfAttendance)).hasChild(String.valueOf(monthOfAttendance))) {
                        for (DataSnapshot f : snapshot.child("CLASS").child(CLASS_ID).child("students").child(keyStudent).child("DATES").child(String.valueOf(yearOfAttendance)).child(String.valueOf(monthOfAttendance)).getChildren()) {
                            int dayOfMonth = Integer.parseInt(f.getKey().toString());
                            Log.d("DAY", String.valueOf(dayOfMonth));
                            String statusOfAtt = f.child("attendance").getValue().toString();
                            System.out.println("STATUS: " + statusOfAtt);
                            int i = Integer.valueOf(statusOfAtt);
                            char temp;
                            if (i == 1) {
                                temp = 'P';
                            } else if (i == -1) {
                                temp = 'A';
                            } else {
                                temp = '-';
                            }
                            secondHash.put(dayOfMonth, temp);
                        }
                        firstHash.put(studentName, secondHash);

                    }
                    //}
                    listOfStudentAndAttendance.add(firstHash);

                }
                saveData.setListOfStudentAndAttendance(listOfStudentAndAttendance);
                try {
                    createPdf(saveData);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }


            private void createPdf(PdfDataParticulars save) throws FileNotFoundException, DocumentException {

                File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "Attendance Recorder");
                if (!pdfFolder.exists()) {
                    pdfFolder.mkdir();
                }
                String filename = save.getClass_name() + "_" + save.getSem() + "_" + save.getMonth() + "_attendance";
                File myFile = new File(pdfFolder + "/" + filename + ".pdf");
                OutputStream output = new FileOutputStream(myFile);

                Document document = new Document();
                document.setPageSize(PageSize.A4.rotate());
                PdfWriter.getInstance(document, output);
                document.open();
//Defining Fonts
                Font font1 = new Font(Font.FontFamily.HELVETICA, 36, Font.BOLD);
                Font columnParticularFont = new Font(Font.FontFamily.TIMES_ROMAN, 9);

                Paragraph title = new Paragraph("ATTENDANCE SHEET", font1);
                title.setAlignment(Element.ALIGN_CENTER);
                Paragraph newLine = new Paragraph(" ");

                document.add(title);
                document.add(newLine);
                PdfPTable studentAttTable = new PdfPTable(36);
                PdfPTable classParticularsTable = new PdfPTable(4);

                classParticularsTable.setWidthPercentage(100f);


                float[] columnWidthParticulars = {1f, 2f, 1f, 2f};
                classParticularsTable.setWidths(columnWidthParticulars);
                PdfPCell subject = new PdfPCell(new Paragraph("SUBJECT/COURSE", columnParticularFont));
                PdfPCell month = new PdfPCell((new Paragraph("MONTH/YEAR", columnParticularFont)));
                PdfPCell school = new PdfPCell((new Paragraph("SCHOOL/DEPARTMENT", columnParticularFont)));
                PdfPCell strengthOfClass = new PdfPCell((new Paragraph("TOTAL STRENGTH", columnParticularFont)));
                PdfPCell classStarted = new PdfPCell((new Paragraph("CLASS STARTED", columnParticularFont)));
                PdfPCell teacher = new PdfPCell((new Paragraph("TEACHER", columnParticularFont)));
                PdfPCell semester = new PdfPCell((new Paragraph("SEMESTER/YEAR", columnParticularFont)));
                PdfPCell generatedOn = new PdfPCell((new Paragraph("GENERATED ON", columnParticularFont)));
                PdfPCell space = new PdfPCell((new Paragraph(" ", columnParticularFont)));

                classParticularsTable.addCell(subject);
                classParticularsTable.addCell(save.getSubject_name());
                classParticularsTable.addCell(month);
                classParticularsTable.addCell(save.getMonth());
                classParticularsTable.addCell(school);
                classParticularsTable.addCell(save.getDepartmentName());
                classParticularsTable.addCell(strengthOfClass);
                classParticularsTable.addCell(save.getStrength());
                classParticularsTable.addCell(classStarted);
                classParticularsTable.addCell(save.getClassStarted());

                classParticularsTable.addCell(teacher);
                classParticularsTable.addCell(save.getTeacher());
                classParticularsTable.addCell(semester);
                classParticularsTable.addCell(save.getSem());
                classParticularsTable.addCell(generatedOn);
                classParticularsTable.addCell(date + "-" + monthInt + "-" + year);
                document.add(classParticularsTable);
                document.add(newLine);


                PdfPCell studentName = new PdfPCell(new Paragraph("STUDENT NAME", columnParticularFont));
                PdfPCell absent = new PdfPCell((new Paragraph("ABSENT", columnParticularFont)));
                PdfPCell present = new PdfPCell((new Paragraph("PRESENT", columnParticularFont)));
                PdfPCell totalAtt = new PdfPCell((new Paragraph("TOTAL", columnParticularFont)));
                PdfPCell totalPercentAtt = new PdfPCell((new Paragraph("% ATTENDED", columnParticularFont)));
                float[] columnWidth = {20f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f,
                        3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f
                        , 3f, 3f, 3f, 11f, 11f, 11f, 11f};

                studentAttTable.setWidths(columnWidth);
                studentName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                studentName.setHorizontalAlignment(Element.ALIGN_CENTER);
                absent.setVerticalAlignment(Element.ALIGN_MIDDLE);
                absent.setHorizontalAlignment(Element.ALIGN_CENTER);
                present.setVerticalAlignment(Element.ALIGN_MIDDLE);
                present.setHorizontalAlignment(Element.ALIGN_CENTER);
                totalAtt.setVerticalAlignment(Element.ALIGN_MIDDLE);
                totalAtt.setHorizontalAlignment(Element.ALIGN_CENTER);
                totalPercentAtt.setVerticalAlignment(Element.ALIGN_MIDDLE);
                totalPercentAtt.setHorizontalAlignment(Element.ALIGN_CENTER);
                studentAttTable.addCell(studentName);
                studentAttTable.setWidthPercentage(100f);

                for (int i = 1; i <= 31; i++) {
                    PdfPCell day = new PdfPCell(new Paragraph(String.valueOf(i), columnParticularFont));
                    day.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    day.setHorizontalAlignment(Element.ALIGN_CENTER);
                    studentAttTable.addCell(day);
                }


                studentAttTable.addCell(absent);
                studentAttTable.addCell(present);
                studentAttTable.addCell(totalAtt);
                studentAttTable.addCell(totalPercentAtt);


                ArrayList<HashMap<String, HashMap<Integer, Character>>> listOfStudentAndAttendance;
                listOfStudentAndAttendance = save.getListOfStudentAndAttendance();
                for (HashMap<String, HashMap<Integer, Character>> h : listOfStudentAndAttendance) {
                    for (String key : h.keySet()) {
                        System.out.println(key);
                        studentAttTable.addCell(key);
                        HashMap<Integer, Character> h1 = h.get(key);
                        HashMap<Integer, Character> copy = new HashMap<>();
                        for (int p = 1; p <= 31; p++) {
                            copy.put(p, ' ');
                        }
                        for (Integer x : h1.keySet()) {
                            copy.put(x, h1.get(x));

                        }
                        for (int l = 1; l <= 31; l++) {
                            studentAttTable.addCell(String.valueOf(copy.get(l)));
                        }


                        int pre = 0, ab = 0, total = 0;
                        double percent;
                        for (int l = 1; l <= 31; l++) {
                            if (copy.get(l).equals('P')) {
                                total++;
                                pre++;
                            }
                            if (copy.get(l).equals('A')) {
                                total++;
                                ab++;
                            }
                        }


                        percent = (double) pre / total * 100;

                        studentAttTable.addCell(String.valueOf(ab));
                        studentAttTable.addCell(String.valueOf(pre));
                        studentAttTable.addCell(String.valueOf(total));
                        studentAttTable.addCell(String.format("%.2f", percent));
                    }
                }
                document.add(studentAttTable);
                document.close();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_print_attendance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroupSelectPrint = view.findViewById(R.id.radioGroupSelectPrint);
        CLASS_ID = getArguments().getString("classID");
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("USERS");
        radioGroupSelectPrint.setOnCheckedChangeListener(this);
        final Calendar calendar = Calendar.getInstance();
        generatePdfButton = view.findViewById(R.id.generatePdfButton);
        year = calendar.get(Calendar.YEAR);
        monthInt = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DATE);
        generatePdfButton.setOnClickListener(this);


        //get the spinner from the xml.
        dropdown = view.findViewById(R.id.spinner);
//create a list of items for the spinner.
        dropdown.setOnItemSelectedListener(this);
        databaseRef.child(mAuth.getCurrentUser().getUid()).child("CLASS").child(CLASS_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String classStarted = snapshot.child("classStarted").getValue().toString();
                Calendar c = Calendar.getInstance();
                String[] dateStart = classStarted.split("-");
                System.out.println("Year= " + dateStart[2]);
                System.out.println("month= " + dateStart[1]);

                int yearEnd = c.get(Calendar.YEAR);
                int monthEnd = c.get(Calendar.MONTH) + 1;
                dropList = new ArrayList<>();
                if (Integer.parseInt(dateStart[2]) == yearEnd) {

                    for (int i = Integer.valueOf(dateStart[1]); i <= monthEnd; i++) {
                        String monthString = new DateFormatSymbols().getMonths()[i - 1];
                        monthString += ", " + dateStart[2];
                        dropList.add(monthString);
                    }

                } else if (Integer.parseInt(dateStart[2]) < yearEnd) {
                    int i = yearEnd - Integer.parseInt(dateStart[2]) + 1;
                    monthEnd += (yearEnd - Integer.parseInt(dateStart[2])) * 12;
                    Log.d("POI", String.valueOf(monthEnd - Integer.valueOf(dateStart[1])));
                    for (int j = Integer.valueOf(dateStart[1]); j <= monthEnd; j++) {
                        String monthString = new DateFormatSymbols().getMonths()[(j - 1) % 12];
                        String monthYearString = monthString + ", " + dateStart[2];
                        dropList.add(monthYearString);
                        if (monthString.equals("December")) {
                            int y = Integer.parseInt(dateStart[2]);
                            y++;
                            dateStart[2] = String.valueOf(y);
                        }

                    }

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, dropList);
                dropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generatePdfButton: {

                if (radioPressed && monthToPrint != -1 && yearToPrint != -1 ) {
                    monthAttendance(monthToPrint, yearToPrint);
                } else if(radioPressed && monthToPrint == -1 && yearToPrint == -1){
                    overallAttendance();
                }

                break;
            }
        }

    }

    private void overallAttendance() {
        databaseRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OverallAttendanceParticulars saveData = new OverallAttendanceParticulars();
                String departmentName = snapshot.child("departmentName").getValue().toString();
                String teacherName = snapshot.child("name").getValue().toString();
                String class_name = snapshot.child("CLASS").child(CLASS_ID).child("class_name").getValue().toString();
                String subject_name = snapshot.child("CLASS").child(CLASS_ID).child("subject_name").getValue().toString();
                String strength = snapshot.child("CLASS").child(CLASS_ID).child("strength").getValue().toString();
                String classStarted = snapshot.child("CLASS").child(CLASS_ID).child("classStarted").getValue().toString();
                String semester = snapshot.child("CLASS").child(CLASS_ID).child("semester").getValue().toString();
                String monthString = "Overall";
                saveData.setDepartmentName(departmentName);
                saveData.setTeacher(teacherName);
                saveData.setClass_name(class_name);
                saveData.setSubject_name(subject_name);
                saveData.setStrength(strength);
                saveData.setClassStarted(classStarted);
                saveData.setMonth(monthString);
                saveData.setSem(semester);
                ArrayList<HashMap<String, HashMap<String, Double>>> listOfStudentAndOverallAttendance = new ArrayList<>();

                for (DataSnapshot d : snapshot.child("CLASS").child(CLASS_ID).child("students").getChildren()) {
                    HashMap<String, HashMap<String, Double>> h1 = new HashMap<>();
                    String studentKey = d.getKey();
                    String studentName = d.child("studentName").getValue().toString();
                    for (DataSnapshot e : snapshot.child("CLASS").child(CLASS_ID).child("students").child(studentKey).child("DATES").getChildren()) {
                        HashMap<String, Double> h2 = new HashMap<>();
                        String year = e.getKey();
                        for (DataSnapshot f : snapshot.child("CLASS").child(CLASS_ID).child("students").child(studentKey).child("DATES").child(year).getChildren()) {
                            String month = f.getKey();

                            double pre = 0, total = 0;
                            for (DataSnapshot g : snapshot.child("CLASS").child(CLASS_ID).child("students").child(studentKey).child("DATES").child(year).child(month).getChildren()) {
                                String s = g.child("attendance").getValue().toString();
                                if (s.equals("1")) {
                                    pre++;
                                    total++;
                                } else if (s.equals("-1") || s.equals("0")) {
                                    total++;
                                }

                            }
                            double percent = pre / total;
                            h2.put(month + ", " + year, percent * 100);
                        }
                        h1.put(studentName, h2);
                    }
                    listOfStudentAndOverallAttendance.add(h1);
                }
                saveData.setListOfStudentAndOverallAttendance(listOfStudentAndOverallAttendance);
                try {
                    createPdf(saveData);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            public void createPdf(OverallAttendanceParticulars save) throws FileNotFoundException, DocumentException {
                File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "Attendance Recorder");
                if (!pdfFolder.exists()) {
                    pdfFolder.mkdir();
                }
                String filename = save.getClass_name() + "_" + save.getSem() + "_" + save.getSubject_name() + "_" + save.getMonth() + "_attendance";
                File myFile = new File(pdfFolder + "/" + filename + ".pdf");
                OutputStream output = new FileOutputStream(myFile);

                Document document = new Document();
                document.setPageSize(PageSize.A4.rotate());
                PdfWriter.getInstance(document, output);
                document.open();
//Defining Fonts
                Font font1 = new Font(Font.FontFamily.HELVETICA, 36, Font.BOLD);
                Font columnParticularFont = new Font(Font.FontFamily.TIMES_ROMAN, 9);

                Paragraph title = new Paragraph("ATTENDANCE SHEET", font1);
                title.setAlignment(Element.ALIGN_CENTER);
                Paragraph newLine = new Paragraph(" ");

                document.add(title);
                document.add(newLine);

                PdfPTable classParticularsTable = new PdfPTable(4);

                classParticularsTable.setWidthPercentage(100f);


                float[] columnWidthParticulars = {1f, 2f, 1f, 2f};
                classParticularsTable.setWidths(columnWidthParticulars);
                PdfPCell subject = new PdfPCell(new Paragraph("SUBJECT/COURSE", columnParticularFont));
                PdfPCell month = new PdfPCell((new Paragraph("MONTH/YEAR", columnParticularFont)));
                PdfPCell school = new PdfPCell((new Paragraph("SCHOOL/DEPARTMENT", columnParticularFont)));
                PdfPCell strengthOfClass = new PdfPCell((new Paragraph("TOTAL STRENGTH", columnParticularFont)));
                PdfPCell classStarted = new PdfPCell((new Paragraph("CLASS STARTED", columnParticularFont)));
                PdfPCell teacher = new PdfPCell((new Paragraph("TEACHER", columnParticularFont)));
                PdfPCell semester = new PdfPCell((new Paragraph("SEMESTER/YEAR", columnParticularFont)));
                PdfPCell generatedOn = new PdfPCell((new Paragraph("GENERATED ON", columnParticularFont)));
                PdfPCell space = new PdfPCell((new Paragraph(" ", columnParticularFont)));

                classParticularsTable.addCell(subject);
                classParticularsTable.addCell(save.getSubject_name());
                classParticularsTable.addCell(month);
                classParticularsTable.addCell(save.getMonth());
                classParticularsTable.addCell(school);
                classParticularsTable.addCell(save.getDepartmentName());
                classParticularsTable.addCell(strengthOfClass);
                classParticularsTable.addCell(save.getStrength());
                classParticularsTable.addCell(classStarted);
                classParticularsTable.addCell(save.getClassStarted());

                classParticularsTable.addCell(teacher);
                classParticularsTable.addCell(save.getTeacher());
                classParticularsTable.addCell(semester);
                classParticularsTable.addCell(save.getSem());
                classParticularsTable.addCell(generatedOn);
                classParticularsTable.addCell(date + "-" + monthInt + "-" + year);
                document.add(classParticularsTable);
                document.add(newLine);

                PdfPCell studentName = new PdfPCell(new Paragraph("STUDENT NAME", columnParticularFont));
                PdfPCell totalPerAtt = new PdfPCell(new Paragraph("TOTAL % ATTENDED", columnParticularFont));
                int totalColumn = 2 + dropList.size();

                float[] columnWidth = new float[totalColumn];
                columnWidth[0] = 20f;
                for (int i = 1; i <= totalColumn - 1; i++) {
                    columnWidth[i] = 10f;
                }
                PdfPTable studentAttTable = new PdfPTable(totalColumn);
                studentAttTable.setWidths(columnWidth);
                studentAttTable.addCell(studentName);
                studentAttTable.setWidthPercentage(100f);


                ArrayList<HashMap<String, HashMap<String, Double>>> listOfStudentAndOverallAttendance = save.getListOfStudentAndOverallAttendance();

                for (int i = 0; i < totalColumn - 2; i++) {
                    studentAttTable.addCell(dropList.get(i));
                }
                studentAttTable.addCell(totalPerAtt);


                for (HashMap<String, HashMap<String, Double>> h : listOfStudentAndOverallAttendance) {

                    for (String k : h.keySet()) {
                        studentAttTable.addCell(k);
                        HashMap<String, Double> h1 = h.get(k);

                        HashMap<String, Double> copy = new LinkedHashMap<>();
                        for (String s : dropList) {
                            copy.put(s, 0.0);
                        }

                        for (String s : h1.keySet()) {
                            String[] o = s.split(", ");
                            String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(o[0]) - 1];
                            monthString += ", " + o[1];
                            copy.put(monthString, h1.get(s));
                        }
double totalPer=0;
                        int notZero=0;
                        for (String s : copy.keySet()) {
                            String per=copy.get(s).toString();
                            studentAttTable.addCell(per);
                            totalPer+=Double.valueOf(per);

                        }
double finalPer=totalPer/dropList.size();
                        studentAttTable.addCell(String.format("%.2f",finalPer));

                    }
                }
                document.add(studentAttTable);
                document.close();


            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioPressed=true;
        switch (checkedId) {
            case R.id.currentMonthRadioButton:
                dropdown.setVisibility(View.GONE);
                Calendar c = Calendar.getInstance();
                monthToPrint = c.get(Calendar.MONTH) + 1;
                yearToPrint = c.get(Calendar.YEAR);
                break;


            case R.id.overallPrintRadioButton:
                dropdown.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Over", Toast.LENGTH_SHORT).show();
                monthToPrint = -1;
                yearToPrint = -1;
                break;
            case R.id.ofMonthRadioButton:
                //  Toast.makeText(getContext(), "OF", Toast.LENGTH_SHORT).show();
                dropdown.setVisibility(View.VISIBLE);
                break;

            default:
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getMonthNumber(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        monthSelected = dropList.get(position);
        String[] monthYear = monthSelected.split(", ");
        monthToPrint = getMonthNumber(monthYear[0]);
        Toast.makeText(getContext(), String.valueOf(monthToPrint), Toast.LENGTH_SHORT).show();
        yearToPrint = Integer.parseInt(monthYear[1]);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (!dropList.isEmpty()) {
            monthSelected = dropList.get(0);
            String[] monthYear = monthSelected.split(", ");
            monthToPrint = getMonthNumber(monthYear[0]);
            Toast.makeText(getContext(), String.valueOf(monthToPrint), Toast.LENGTH_SHORT).show();
            yearToPrint = Integer.parseInt(monthYear[1]);
        }
    }

}