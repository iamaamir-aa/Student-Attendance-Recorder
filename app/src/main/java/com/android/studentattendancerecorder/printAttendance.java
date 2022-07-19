package com.android.studentattendancerecorder;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.Document;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.fonts.otf.TableHeader;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.DataInput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;


public class printAttendance extends Fragment implements View.OnClickListener {
TextView toDateTV,fromDateTV;
int year,month,date;
Button generatePdfButton;
    DatePickerDialog datePickerDialog;
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

        final Calendar calendar=Calendar.getInstance();
        fromDateTV=view.findViewById(R.id.fromDateTextView);
        toDateTV=view.findViewById(R.id.toDateTextView);
        generatePdfButton=view.findViewById(R.id.generatePdfButton);
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        date=calendar.get(Calendar.DATE);
        fromDateTV.setOnClickListener(this);
        toDateTV.setOnClickListener(this);
        generatePdfButton.setOnClickListener(this);
        month--;
        fromDateTV.setText(date+"-"+month+"-"+year);
        month++;
        toDateTV.setText(date+"-"+month+"-"+year);


  }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fromDateTextView: datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                   fromDateTV.setText(dayOfMonth+"-"+month+"-"+year);
                }
            },year,month,date);
            datePickerDialog.show();
            break;
            case R.id.toDateTextView: datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                   toDateTV.setText(dayOfMonth+"-"+month+"-"+year);
                }
            },year,month,date);
            datePickerDialog.show();
            break;
            case R.id.generatePdfButton:{
                try {
                    createPdf();

                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Attendance Recorder");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
        }


        File myFile = new File(pdfFolder +"/aamir" + ".pdf");

        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document();

        PdfWriter.getInstance(document, output);

        document.open();
        PdfPTable table = new PdfPTable(3);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
        PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));
        PdfPCell cell4 = new PdfPCell(new Paragraph("Cell 4"));
        PdfPCell cell5 = new PdfPCell(new Paragraph("Cell 5"));
        PdfPCell cell6 = new PdfPCell(new Paragraph("Cell 6"));
        PdfPCell cell7 = new PdfPCell(new Paragraph("Cell 7"));
        PdfPCell cell8 = new PdfPCell(new Paragraph("Cell 8"));
        PdfPCell cell9 = new PdfPCell(new Paragraph("Cell 9"));
        //PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);
        table.addCell(cell8);
        table.addCell(cell9);;

        document.add(table);


        //Step 5: Close the document
        document.close();



    }
}