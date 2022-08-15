package com.android.studentattendancerecorder;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForStudentList;
import com.android.studentattendancerecorder.DialogueBox.AddStudent;
import com.android.studentattendancerecorder.Model.Dates;
import com.android.studentattendancerecorder.Model.StudentsDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class StudentListFragment extends Fragment implements DialogInterface.OnDismissListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {


    private RecyclerView recyclerViewStudent;
    private RecyclerViewAdapterForStudentList recyclerViewAdapterForStudentList;
    private ArrayList<StudentsDetail> studentDetailsArrayList;
    int year, month, date;
    DatePickerDialog datePickerDialog;
    RadioGroup radioGroup;
    TextView changeDateTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private String CLASS_ID;

    private ProgressBar spinner;
    private TextView defaultDate;


    public void showSpinner() {
        spinner.setVisibility(View.VISIBLE);
    }

    public void hideSpinner() {
        spinner.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_student, menu);
    }

    public void updateUI() {
        Bundle bundle = new Bundle();
        bundle.putString("classID", CLASS_ID);
        NavHostFragment.findNavController(StudentListFragment.this)
                .navigate(R.id.action_studentListFragment_to_printAttendance, bundle);
    }


    private void updateList(int i) {
        showSpinner();
        databaseRef.child(mAuth.getCurrentUser().getUid()).child("CLASS").child(CLASS_ID).child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentDetailsArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getRef().getKey();
                    String studentName = dataSnapshot.child("studentName").getValue().toString();
                    String enrollmentNumber = dataSnapshot.child("enrollmentNumber").getValue().toString();
                    ArrayList<Dates> ran = new ArrayList<>();
                    System.out.println(key);
                    for (DataSnapshot d : snapshot.child(key).child("DATES").getChildren()) {
                        String yearParse = d.getRef().getKey();
                        for (DataSnapshot e : snapshot.child(key).child("DATES").child(yearParse).getChildren()) {
                            String monthParse = e.getKey();
                            for (DataSnapshot f : snapshot.child(key).child("DATES").child(yearParse).child(monthParse).getChildren()) {
                                String dayParse = f.getKey();
                                int noOfAtt = Integer.parseInt(f.child("attendance").getValue().toString());
                                System.out.println(dayParse + "/" + monthParse + "/" + yearParse + "-" + noOfAtt);
                                ran.add(new Dates(Integer.parseInt(yearParse), Integer.parseInt(monthParse), Integer.parseInt(dayParse), noOfAtt));
                            }






                        }

                    }
                    StudentsDetail newClass = new StudentsDetail(studentName, enrollmentNumber, key, ran);

                    studentDetailsArrayList.add(newClass);}
                recyclerViewAdapterForStudentList.notifyDataSetChanged();
                hideSpinner();}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.print:
                updateUI();
                return true;
            case R.id.changeDate:
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DATE);
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearInside, int monthInside, int dayOfMonth) {
                        monthInside++;
                        year = yearInside;
                        month = monthInside;
                        date = dayOfMonth;
                        recyclerViewAdapterForStudentList = new RecyclerViewAdapterForStudentList(getContext(),
                                studentDetailsArrayList, StudentListFragment.this,
                                new Dates(yearInside, monthInside, dayOfMonth, 0), CLASS_ID);
                        recyclerViewStudent.setHasFixedSize(true);
                        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewStudent.setAdapter(recyclerViewAdapterForStudentList);
                        defaultDate.setText(dayOfMonth + "-" + monthInside + "-" + yearInside);
                        updateList(0);


                    }
                }, year, month, date);


                databaseRef.child(mAuth.getCurrentUser().getUid()).child("CLASS").child(CLASS_ID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      String dateStarted =snapshot.child("classStarted").getValue().toString();
String[] g=dateStarted.split("-");
                        calendar.set(Integer.parseInt(g[2]), Integer.parseInt(g[1])-1, Integer.parseInt(g[0]));
                        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                datePickerDialog.show();


                return true;
            default:
                return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialization
        spinner = (ProgressBar) view.findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("USERS");
        CLASS_ID = StudentListFragmentArgs.fromBundle(getArguments()).getClassID();
        studentDetailsArrayList = new ArrayList<>();
        FloatingActionButton fabToCreateStudent = view.findViewById(R.id.floatingActionButtonAddStudent);
        radioGroup = view.findViewById(R.id.radioGroupNumberOfAtt);
        changeDateTextView = view.findViewById(R.id.studentListDateTextView);
        recyclerViewStudent = view.findViewById(R.id.recyclerViewStudentList);


        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DATE);

        defaultDate = view.findViewById(R.id.studentListDateTextView);
        defaultDate.setText(date + "-" + month + "-" + year);
        Dates date1 = new Dates(year, month, date, 0);

        recyclerViewAdapterForStudentList = new RecyclerViewAdapterForStudentList(getContext(), studentDetailsArrayList, StudentListFragment.this, date1, CLASS_ID);

        updateList(1);

        recyclerViewStudent.setHasFixedSize(true);
        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewStudent.setAdapter(recyclerViewAdapterForStudentList);

        //Listener
        fabToCreateStudent.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);


    }

    private void showAddStudentDialog() {
        AddStudent dialogueFragment = new AddStudent(CLASS_ID);
        dialogueFragment.show(getChildFragmentManager(), "AddStudent");
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        showSpinner();
        updateList(2);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radioButtonX1) {
            Toast.makeText(getActivity(), "X1", Toast.LENGTH_SHORT).show();
        }
        if (checkedId == R.id.radioButtonX2) {
            Toast.makeText(getActivity(), "X2", Toast.LENGTH_SHORT).show();
        }
        if (checkedId == R.id.radioButtonX3) {
            Toast.makeText(getActivity(), "X3", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButtonAddStudent: {
                showAddStudentDialog();
                break;
            }
        }


    }
}