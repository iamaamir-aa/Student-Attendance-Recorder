package com.android.studentattendancerecorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddClassDialogueFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v1=getLayoutInflater().inflate(R.layout.addclassdialogue,null);
        builder.setView(v1);
 Button b1=v1.findViewById(R.id.addButtonInStudent);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"ADD CLICKED",Toast.LENGTH_SHORT).show();
            }
        });







        return builder.create();
    }


}
