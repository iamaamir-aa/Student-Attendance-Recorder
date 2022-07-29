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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
    TextView toDateTV, fromDateTV;
    int year, month, date;
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

        final Calendar calendar = Calendar.getInstance();
        fromDateTV = view.findViewById(R.id.fromDateTextView);
        toDateTV = view.findViewById(R.id.toDateTextView);
        generatePdfButton = view.findViewById(R.id.generatePdfButton);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DATE);
        fromDateTV.setOnClickListener(this);
        toDateTV.setOnClickListener(this);
        generatePdfButton.setOnClickListener(this);
        month--;
        fromDateTV.setText(date + "-" + month + "-" + year);
        month++;
        toDateTV.setText(date + "-" + month + "-" + year);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromDateTextView:
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fromDateTV.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                }, year, month, date);
                datePickerDialog.show();
                break;
            case R.id.toDateTextView:
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        toDateTV.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                }, year, month, date);
                datePickerDialog.show();
                break;
            case R.id.generatePdfButton: {
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


        File myFile = new File(pdfFolder + "/aamir" + ".pdf");
        Font font1 = new Font(Font.FontFamily.HELVETICA, 36, Font.BOLD);
        Font columnParticularFont = new Font(Font.FontFamily.TIMES_ROMAN, 9);

        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());

        PdfWriter.getInstance(document, output);

        document.open();
        Paragraph title = new Paragraph("ATTENDANCE SHEET", font1);
        title.setAlignment(Element.ALIGN_CENTER);
        Paragraph newLine = new Paragraph(" ");

        document.add(title);
        document.add(newLine);
        PdfPTable studentAttTable = new PdfPTable(36);
        PdfPTable classParticularsTable = new PdfPTable(4);
        PdfPCell studentName = new PdfPCell(new Paragraph("STUDENT NAME", columnParticularFont));

        PdfPCell absent = new PdfPCell((new Paragraph("ABSENT", columnParticularFont)));
        PdfPCell present = new PdfPCell((new Paragraph("PRESENT", columnParticularFont)));
        PdfPCell totalAtt = new PdfPCell((new Paragraph("TOTAL", columnParticularFont)));
        PdfPCell totalPercentAtt = new PdfPCell((new Paragraph("% ATTENDED", columnParticularFont)));
        float[] columnWidth = {20f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 3f, 3f,
                3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f
                , 3f, 3f, 3f, 7f, 8f, 6f, 10f};
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
        document.add(studentAttTable);

        //Step 5: Close the document
        document.close();


    }
}