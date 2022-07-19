package com.android.studentattendancerecorder;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForStudentList;
import com.android.studentattendancerecorder.Model.StudentsDetail;
import com.android.studentattendancerecorder.databinding.FragmentSecondBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;


public class StudentListFragment extends Fragment {


    private RecyclerView recyclerViewStudent;
    private RecyclerViewAdapterForStudentList recyclerViewAdapterForStudentList;
    private ArrayList<StudentsDetail> studentDetailsArrayList;
    int year,month,date;
DatePickerDialog datePickerDialog;
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_student,menu);
    }
public  void updateUI(){
    NavHostFragment.findNavController(StudentListFragment.this)
            .navigate(R.id.action_studentListFragment_to_printAttendance);
}
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.print:updateUI();return true;
            case R.id.changeDate:
                final Calendar calendar=Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH)+1;
                date=calendar.get(Calendar.DATE);
                datePickerDialog=new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> Log.d("DATE PICKED:",dayOfMonth+"/"+month+"/"+year),year,month,date);
                datePickerDialog.show();
                ;return true;
            default: return false;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String KEY=StudentListFragmentArgs.fromBundle(getArguments()).getClassID();
        Log.d("KEY:",KEY);

        recyclerViewStudent = view.findViewById(R.id.recyclerViewStudentList);
        studentDetailsArrayList=new ArrayList<StudentsDetail>();

        recyclerViewStudent.setHasFixedSize(true);
        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAdapterForStudentList=new RecyclerViewAdapterForStudentList(getContext(),studentDetailsArrayList,StudentListFragment.this);
        recyclerViewStudent.setAdapter(recyclerViewAdapterForStudentList);

        ConstraintLayout  constraintLayoutStudentList=view.findViewById(R.id.constraintLayoutStudentList);
        LinearLayout linearLayoutCreateStudent=view.findViewById(R.id.linearLayoutAddingStudent);


        Button cancelStudentButton = view.findViewById(R.id.cancelStudentButton);
        Button addStudentButton = view.findViewById(R.id.addStudentButton);
        FloatingActionButton fabToCreateStudent = view.findViewById(R.id.floatingActionButtonAddStudent);
        fabToCreateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutStudentList.setVisibility(View.INVISIBLE);
                linearLayoutCreateStudent.setVisibility(View.VISIBLE);
            }
        });
        cancelStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                constraintLayoutStudentList.setVisibility(View.VISIBLE);
                linearLayoutCreateStudent.setVisibility(View.INVISIBLE);
            }
        });
        EditText studentName = view.findViewById(R.id.editTextStudentName);

        EditText enrollmentNumber = view.findViewById(R.id.editTextEnrollmentNumber);
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentDetailsArrayList.add(new StudentsDetail(studentName.getText().toString(),
                        enrollmentNumber.getText().toString(),true));
                recyclerViewAdapterForStudentList.notifyDataSetChanged();
                linearLayoutCreateStudent.setVisibility(View.INVISIBLE);
                constraintLayoutStudentList.setVisibility(View.VISIBLE);


            }
        });

    }


}