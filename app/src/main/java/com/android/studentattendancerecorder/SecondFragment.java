package com.android.studentattendancerecorder;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForClassList;
import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.databinding.FragmentSecondBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private RecyclerView recyclerViewClass;
    private RecyclerViewAdapterForClassList recyclerViewAdapterForClassList;
    private ArrayList<ClassAndSubjectDetails> classAndSubjectDetailsArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private FragmentSecondBinding binding;

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
//registerForContextMenu(recyclerViewClass);
        recyclerViewClass = view.findViewById(R.id.recyclerViewClass);
        recyclerViewClass.setHasFixedSize(true);
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        classAndSubjectDetailsArrayList = new ArrayList<ClassAndSubjectDetails>();

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA I", "Operating System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II", "Oops"));
        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II", "Oops"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("M.Tech", "System Design"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("B.tech.", "Computer Network"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA", "Teleport System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II", "Operating System"));
        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II", "Oops"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("M.Tech", "System Design"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("B.tech.", "Computer Network"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA", "Teleport System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II", "Operating System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("M.Tech", "System Design"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("B.tech.", "Computer Network"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA", "Teleport System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II", "Operating System"));
        recyclerViewAdapterForClassList = new RecyclerViewAdapterForClassList(getContext(), classAndSubjectDetailsArrayList, SecondFragment.this);
        recyclerViewClass.setAdapter(recyclerViewAdapterForClassList);
        ConstraintLayout constraintLayoutClassList = view.findViewById(R.id.constraintLayoutClass);
        final LinearLayout linearLayoutCreateClass = view.findViewById(R.id.linearLayoutAddingClass);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button addButton = view.findViewById(R.id.addButton);
        FloatingActionButton fabToCreateClass = view.findViewById(R.id.floatingActionButtonToAddClass);
        fabToCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutClassList.setVisibility(View.INVISIBLE);
                linearLayoutCreateClass.setVisibility(View.VISIBLE);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                constraintLayoutClassList.setVisibility(View.VISIBLE);
                linearLayoutCreateClass.setVisibility(View.INVISIBLE);
            }
        });
        EditText className = view.findViewById(R.id.editTextClassName);

        EditText subjectName = view.findViewById(R.id.editTextSubjectName);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(className.getText().toString()!="" || subjectName.getText().toString() !="" ){
                    classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails(className.getText().toString(),
                            subjectName.getText().toString()));
                    recyclerViewAdapterForClassList.notifyDataSetChanged();
            //    }

                linearLayoutCreateClass.setVisibility(View.INVISIBLE);
                constraintLayoutClassList.setVisibility(View.VISIBLE);


            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}