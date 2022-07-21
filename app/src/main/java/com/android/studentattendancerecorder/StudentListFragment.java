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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    RadioGroup radioGroup;
    TextView changeDateTextView;
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
                datePickerDialog=new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> changeDateTextView.setText(dayOfMonth+"-"+(month+1)+"-"+year),year,month,date);
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
       // String KEY=StudentListFragmentArgs.fromBundle(getArguments()).getClassID();
       // Log.d("KEY:",KEY);
radioGroup=view.findViewById(R.id.radioGroupNumberOfAtt);
changeDateTextView=view.findViewById(R.id.studentListDateTextView);
radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
if(checkedId==R.id.radioButtonX1){
    Toast.makeText(getActivity(), "X1", Toast.LENGTH_SHORT).show();
}if(checkedId==R.id.radioButtonX2){

    AddClassDialogueFragment dialogueFragment=new AddClassDialogueFragment();
    dialogueFragment.show(getParentFragmentManager(),null);

        }if(checkedId==R.id.radioButtonX3){
            Toast.makeText(getActivity(), "X3", Toast.LENGTH_SHORT).show();

        }
    }
});
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
        EditText studentName = view.findViewById(R.id.editTextStudentName);

        EditText enrollmentNumber = view.findViewById(R.id.editTextEnrollmentNumber);
        cancelStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               constraintLayoutStudentList.setVisibility(View.VISIBLE);
                linearLayoutCreateStudent.setVisibility(View.INVISIBLE);
                studentName.setText("");
                enrollmentNumber.setText("");
            }
        });

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentDetailsArrayList.add(new StudentsDetail(studentName.getText().toString(),
                        enrollmentNumber.getText().toString(),true));
                recyclerViewAdapterForStudentList.notifyDataSetChanged();
                linearLayoutCreateStudent.setVisibility(View.INVISIBLE);
                constraintLayoutStudentList.setVisibility(View.VISIBLE);

                studentName.setText("");
                enrollmentNumber.setText("");

            }
        });

    }


}