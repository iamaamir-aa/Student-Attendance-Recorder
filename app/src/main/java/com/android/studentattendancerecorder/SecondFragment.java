package com.android.studentattendancerecorder;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForClassList;
import com.android.studentattendancerecorder.DialogueBox.AddClass;
import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.databinding.FragmentSecondBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SecondFragment extends Fragment implements View.OnClickListener, DialogInterface.OnDismissListener {
    private RecyclerView recyclerViewClass;
    private RecyclerViewAdapterForClassList recyclerViewAdapterForClassList;
    private List<ClassAndSubjectDetails> classAndSubjectDetailsArrayList;
    private FragmentSecondBinding binding;
    private FirebaseAuth mAuth;
    private ConstraintLayout constraintLayoutClassList;
    private LinearLayout linearLayoutCreateClass;
    private DatabaseReference databaseRef;
    Button addButton,cancelButton;
    EditText className,subjectName;
    TextView selectStartDate;
    int year,month,date;
    DatePickerDialog datePickerDialog;
    private ProgressBar spinner;


public void showSpinner(){
    spinner.setVisibility(View.VISIBLE);
}
public void hideSpinner(){
    spinner.setVisibility(View.GONE);
}

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_class,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.signOutMenuButton:mAuth.signOut();updateUI(mAuth.getCurrentUser());
            return true;
            case R.id.about: return false;
            default:
                Toast.makeText(getActivity(), "Incorrect Selection", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
    private void updateList(){
        databaseRef.child(mAuth.getCurrentUser().getUid()).child("CLASS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classAndSubjectDetailsArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String key=dataSnapshot.getRef().getKey();
                    String class_name=dataSnapshot.child("class_name").getValue().toString();
                    String subject_name=dataSnapshot.child("subject_name").getValue().toString();
                    ClassAndSubjectDetails newClass=new ClassAndSubjectDetails(class_name,subject_name,key);
                    classAndSubjectDetailsArrayList.add(newClass);
                }
                recyclerViewAdapterForClassList.notifyDataSetChanged();
                spinner.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user==null) {
            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = (ProgressBar)view.findViewById(R.id.progressBar);
showSpinner();
        mAuth=FirebaseAuth.getInstance();
        databaseRef =FirebaseDatabase.getInstance().getReference("USERS");
        recyclerViewClass = view.findViewById(R.id.recyclerViewClass);
        recyclerViewClass.setHasFixedSize(true);
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        classAndSubjectDetailsArrayList = new ArrayList<>();
        databaseRef.child(mAuth.getCurrentUser().getUid()).child("CLASS");
        recyclerViewAdapterForClassList = new RecyclerViewAdapterForClassList(getContext(), classAndSubjectDetailsArrayList, SecondFragment.this);
        recyclerViewClass.setAdapter(recyclerViewAdapterForClassList);
        updateList();

        constraintLayoutClassList = view.findViewById(R.id.constraintLayoutClass);
        linearLayoutCreateClass = view.findViewById(R.id.linearLayoutAddingClass);
        cancelButton = view.findViewById(R.id.cancelButton);
        addButton = view.findViewById(R.id.addButton);
        className = view.findViewById(R.id.editTextClassName);
        subjectName = view.findViewById(R.id.editTextSubjectName);
        FloatingActionButton fabToCreateClass = view.findViewById(R.id.floatingActionButtonToAddClass);
        fabToCreateClass.setOnClickListener(this);
        final Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        date=calendar.get(Calendar.DATE);
        selectStartDate=view.findViewById(R.id.selectStartDate);
        selectStartDate.setOnClickListener(this);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.floatingActionButtonToAddClass: {
                AddClass dialogueFragment = new AddClass();
                dialogueFragment.show(getChildFragmentManager(), "AddClass");

                break;
            }
            default:break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        showSpinner();
        updateList();
    }
}