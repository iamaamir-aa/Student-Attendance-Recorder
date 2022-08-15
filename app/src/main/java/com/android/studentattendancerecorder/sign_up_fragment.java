package com.android.studentattendancerecorder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class sign_up_fragment extends Fragment {
    public Button saveSignUpButton;
    public EditText emailEditText;
    public EditText passwordEditText,editTextPersonName,editTextDepartmentName;
    private FirebaseAuth mAuth;
    private EditText confirmPasswordEditText;
    private DatabaseReference databaseRef;
    private String name;
    private String departmentName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      mAuth = FirebaseAuth.getInstance();

        emailEditText=view.findViewById(R.id.emailEditText);
        passwordEditText=view.findViewById(R.id.passwordEditText);
editTextPersonName=view.findViewById(R.id.editTextTextPersonName);
        confirmPasswordEditText=view.findViewById(R.id.confirmPasswordEditText);
        saveSignUpButton=view.findViewById(R.id.saveSignUpDetailsButton);
editTextDepartmentName=view.findViewById(R.id.editTextTextDepartmentName);












    saveSignUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email=emailEditText.getText().toString();
            String password=passwordEditText.getText().toString();
            name=editTextPersonName.getText().toString();
            departmentName=editTextDepartmentName.getText().toString();
            String confirmPassword=confirmPasswordEditText.getText().toString();
            if(email.equals("")||password.equals("")||confirmPassword.equals("")||name.equals("")||departmentName.equals("")){
                Snackbar.make(getView(),"ENTER ALL FIELDS",Snackbar.LENGTH_SHORT).show();
            }else{

                if(password.equals(confirmPassword)){
                    createAccount(email,password);
                }else{
                    Snackbar.make(getView(),"PASSWORD NOT MATCHED",Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    });
    }
    public void updateUI(FirebaseUser user){
        NavHostFragment.findNavController(sign_up_fragment.this).navigate(R.id.action_sign_up_fragment_to_FirstFragment);
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SEE", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference().child("USERS").child(mAuth.getCurrentUser().getUid()).child("name").setValue(name);
                            FirebaseDatabase.getInstance().getReference().child("USERS").child(mAuth.getCurrentUser().getUid()).child("departmentName").setValue(departmentName);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SEE", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

}