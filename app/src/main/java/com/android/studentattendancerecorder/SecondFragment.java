package com.android.studentattendancerecorder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForClassList;
import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.databinding.FragmentSecondBinding;

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

        recyclerViewClass= view.findViewById(R.id.recyclerViewClass);
        recyclerViewClass.setHasFixedSize(true);
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(getActivity()));
classAndSubjectDetailsArrayList=new ArrayList<ClassAndSubjectDetails>();
classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA I","Operating System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II","Oops"));
        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II","Oops"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("M.Tech","System Design"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("B.tech.","Computer Network"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA","Teleport System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II","Operating System"));
        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II","Oops"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("M.Tech","System Design"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("B.tech.","Computer Network"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA","Teleport System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II","Operating System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("M.Tech","System Design"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("B.tech.","Computer Network"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA","Teleport System"));

        classAndSubjectDetailsArrayList.add(new ClassAndSubjectDetails("MCA II","Operating System"));



        recyclerViewAdapterForClassList=new RecyclerViewAdapterForClassList(getContext(),classAndSubjectDetailsArrayList,SecondFragment.this);
        recyclerViewClass.setAdapter(recyclerViewAdapterForClassList);


      /*  binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}