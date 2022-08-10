package com.android.studentattendancerecorder;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class printAttendance extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    TextView toDateTV, fromDateTV;
    int year, monthInt, date;
    Button generatePdfButton;
    DatePickerDialog datePickerDialog;
    RadioGroup radioGroupSelectPrint;
    private Spinner dropdown;
    private String CLASS_ID;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private PdfDataParticulars save;


    private void month(int month) {

        databaseRef.child(mAuth.getCurrentUser().getUid()).child("CLASS").child(CLASS_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<HashMap<String, ArrayList<HashMap<Integer, Character>>>> listOfStudentAndAttendance = new ArrayList<>();
                String class_name = snapshot.child("class_name").getValue().toString();
                String subject_name = snapshot.child("subject_name").getValue().toString();
                String strength = snapshot.child("strength").getValue().toString();
                String classStarted = snapshot.child("classStarted").getValue().toString();

                save.setClass_name(class_name);
                save.setSubject_name(subject_name);
                save.setStrength(strength);
                save.setClassStarted(classStarted);


                for (DataSnapshot d : snapshot.child("students").getChildren()) {
                    HashMap<String, ArrayList<HashMap<Integer, Character>>> firstHash = new HashMap<>();
                    String key = d.getRef().getKey();
                    String student_name = d.child("studentName").getValue().toString();
                    System.out.println(student_name);
                    ArrayList<HashMap<Integer, Character>> secondArrayList = new ArrayList<>();
                    for (DataSnapshot d1 : snapshot.child("students").child(key).child("DATES").getChildren()) {
                        HashMap<Integer, Character> secondHash = new HashMap<>();

                        String date = d1.getRef().getKey();
                        String day = date.substring(0, 2);
                        String month = date.substring(3, 5);
                        String year = date.substring(6, 10);

                        System.out.println(date);
                        save.setMonth(month);
                        System.out.println(month);
                        String o = d1.child("attendance").getValue().toString();
                        System.out.println("AAMIR" + o);
                        int i = Integer.valueOf(o);
                        char temp;
                        if (i == 1) {
                            temp = 'P';
                        } else if (i == -1) {
                            temp = 'A';
                        } else {
                            temp = '-';
                        }

                        secondHash.put(Integer.valueOf(day), temp);
                        secondArrayList.add(secondHash);
                    }
                    firstHash.put(student_name, secondArrayList);
                    listOfStudentAndAttendance.add(firstHash);
                }


                save.setListOfStudentAndAttendance(listOfStudentAndAttendance);
                try {
                    createPdf(save);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }


            }


            private void createPdf(PdfDataParticulars save) throws FileNotFoundException, DocumentException {

                Log.d("HEY THERE", save.toString());

                File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "Attendance Recorder");
                if (!pdfFolder.exists()) {
                    pdfFolder.mkdir();
                }
                File myFile = new File(pdfFolder + "/aamir" + ".pdf");


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
                classParticularsTable.addCell(space);
                classParticularsTable.addCell(strengthOfClass);
                classParticularsTable.addCell(save.getStrength());
                classParticularsTable.addCell(classStarted);
                classParticularsTable.addCell(save.getClassStarted());

                classParticularsTable.addCell(teacher);
                classParticularsTable.addCell(space);
                classParticularsTable.addCell(semester);
                classParticularsTable.addCell(space);
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


                ArrayList<HashMap<String, ArrayList<HashMap<Integer, Character>>>> listOfStudentAndAttendance;
                listOfStudentAndAttendance = save.getListOfStudentAndAttendance();
                for (HashMap<String, ArrayList<HashMap<Integer, Character>>> h : listOfStudentAndAttendance) {
                    for (String key : h.keySet()) {
                        System.out.println(key);
                        studentAttTable.addCell(key);
                        ArrayList<HashMap<Integer, Character>> h1 = h.get(key);
                        HashMap<Integer, Character> copy = new HashMap<>();
                        for (int p = 1; p <= 31; p++) {
                            copy.put(p, ' ');
                        }
                        for (HashMap<Integer, Character> x : h1) {
                            for (Integer k : x.keySet()) {
                                copy.put(k, x.get(k));
                            }
                        }
                        for (int l = 1; l <= 31; l++) {
                            studentAttTable.addCell(String.valueOf(copy.get(l)));
                        }


                        int pre=0,ab=0,total=0;
                        double percent=0.0;
                        for (int l = 1; l <= 31; l++) {
                           if(copy.get(l).equals('P')){
                                total++;
                                pre++;
                            }
                            if(copy.get(l).equals('A')){
total++;
                                ab++;
                            }
                        }


percent=(double) pre/total * 100;

                        studentAttTable.addCell(String.valueOf(ab));
                        studentAttTable.addCell(String.valueOf(pre));
                        studentAttTable.addCell(String.valueOf(total));
                        studentAttTable.addCell(String.valueOf(String.format("%.2f",percent)));
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        save = new PdfDataParticulars();
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

        String[] items = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, items);

        dropdown.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generatePdfButton: {

                month(0);

                break;
            }
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.currentMonthRadioButton:
                dropdown.setVisibility(View.GONE);


                break;


            case R.id.overallPrintRadioButton:
                dropdown.setVisibility(View.GONE);
                Toast.makeText(getContext(), "OVer", Toast.LENGTH_SHORT).show();

                break;
            case R.id.ofMonthRadioButton:
                Toast.makeText(getContext(), "OF", Toast.LENGTH_SHORT).show();
                dropdown.setVisibility(View.VISIBLE);
                break;

            default:
                break;

        }
    }
}