package com.android.studentattendancerecorder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForClassList;
import com.android.studentattendancerecorder.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
public EditText username;
public EditText password;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username=view.findViewById(R.id.editTextUsername);
        password=view.findViewById(R.id.editTextPassword);
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String user_name=username.getText().toString();
                    String pass_word=password.getText().toString();
                    if(user_name.equals("aamir") && pass_word.equals("ansari")){
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    }else{
                        Snackbar.make(getView(),"INCORRECT PASSWORD",Snackbar.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException n){
                    Toast.makeText(getActivity(), "Enter Something", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}