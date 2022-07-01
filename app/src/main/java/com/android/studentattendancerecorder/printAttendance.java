package com.android.studentattendancerecorder;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.DataInput;
import java.util.Calendar;


public class printAttendance extends Fragment implements View.OnClickListener {
TextView toDateTV,fromDateTV;
int year,month,date;
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
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        date=calendar.get(Calendar.DATE);
        fromDateTV.setOnClickListener(this);
        toDateTV.setOnClickListener(this);
        month--;
        fromDateTV.setText(year+"-"+month+"-"+year);
        month++;
        toDateTV.setText(year+"-"+month+"-"+year);


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
        }

    }
}