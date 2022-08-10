package com.android.studentattendancerecorder.DialogueBox;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.studentattendancerecorder.Adapters.RecyclerViewAdapterForStudentList;
import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.Model.Dates;
import com.android.studentattendancerecorder.R;
import com.android.studentattendancerecorder.StudentListFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddClass extends DialogFragment implements View.OnClickListener {
    private View v1;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private int year,month,date;
    private DatePickerDialog datePickerDialog;


    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("USERS");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         v1=getLayoutInflater().inflate(R.layout.addclassdialogue,null);
        builder.setView(v1);
        v1.findViewById(R.id.newClassAdd).setOnClickListener(this);
        v1.findViewById(R.id.newClassCancel).setOnClickListener(this);
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) ;
        date = calendar.get(Calendar.DATE);

        v1.findViewById(R.id.startedOn).setOnClickListener(this);




        return builder.create();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newClassAdd:{
                String class_name=((EditText)v1.findViewById(R.id.classNameEditText)).getText().toString();
                int strength=Integer.parseInt(((EditText)v1.findViewById(R.id.strength)).getText().toString());
                String subject_name=((EditText)v1.findViewById(R.id.subjectNameEditText)).getText().toString();
String startedOn=((TextView)v1.findViewById(R.id.startedOn)).getText().toString();


                if((class_name.equals("") || subject_name.equals("")) || strength==0){
                    Toast.makeText(getContext(),"Provide all fields",Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference newRef=databaseRef.child(mAuth.getCurrentUser().getUid()).child("CLASS").push();
                    String key=newRef.getKey();
                    ClassAndSubjectDetails newClass=new ClassAndSubjectDetails(class_name,subject_name,key,null,startedOn,strength);
                    newRef.setValue(newClass);
                    dismiss();
                }



                break;
            }
            case R.id.newClassCancel:{
                dismiss();
                break;
            }
            case R.id.startedOn:{

                datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        TextView startedOn=v1.findViewById(R.id.startedOn);
                        startedOn.setText(dayOfMonth+"-"+month+"-"+year);

                    }
                },year,month,date);
                datePickerDialog.show();



                break;
            }
        }
    }
}
