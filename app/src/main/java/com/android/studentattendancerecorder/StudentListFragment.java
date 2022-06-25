package com.android.studentattendancerecorder;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForClassList;
import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForStudentList;
import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.Model.StudentsDetail;
import com.android.studentattendancerecorder.databinding.FragmentSecondBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class StudentListFragment extends Fragment {


    private RecyclerView recyclerViewStudent;
    private RecyclerViewAdapterForStudentList recyclerViewAdapterForStudentList;
    private ArrayList<StudentsDetail> studentDetailsArrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerViewStudent = view.findViewById(R.id.recyclerViewStudentList);
        studentDetailsArrayList=new ArrayList<StudentsDetail>();

        recyclerViewStudent.setHasFixedSize(true);
        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(getActivity()));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 2ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 3ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 4ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 5ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 6ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 7ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 8ANSARI","20178976",true));

        studentDetailsArrayList.add(new StudentsDetail("AAMIR 9ANSARI","20178976",true));

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














        /*TextView countTV=view.findViewById(R.id.tv);
        int count=StudentListFragmentArgs.fromBundle(getArguments()).getPosition();
        countTV.setText(String.valueOf(count));
        */




    }


}