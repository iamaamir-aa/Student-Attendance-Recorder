package com.android.studentattendancerecorder.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.Model.Dates;
import com.android.studentattendancerecorder.Model.StudentsDetail;
import com.android.studentattendancerecorder.R;
import com.android.studentattendancerecorder.SecondFragmentDirections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterForStudentList extends RecyclerView.Adapter<RecyclerViewAdapterForStudentList.ViewHolder2> {
    private Context context;
    private List<StudentsDetail> studentDetails;
    private Fragment fragment;
    private ArrayList<String> num;
    private Dates date;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private String CLASS_ID,USER_ID;

    public RecyclerViewAdapterForStudentList(Context context, List<StudentsDetail> studentDetails, Fragment fragment, Dates date, String CLASS_ID) {
        this.context = context;
        this.studentDetails = studentDetails;
        this.fragment = fragment;
        this.num = num;
        this.date = date;
        this.mAuth = mAuth;
        this.databaseRef = databaseRef;
        this.CLASS_ID = CLASS_ID;
        mAuth = FirebaseAuth.getInstance();
        USER_ID=mAuth.getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference("USERS");
    }

    @NonNull
    @Override
    public RecyclerViewAdapterForStudentList.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studentlistitem, parent, false);
        return new RecyclerViewAdapterForStudentList.ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterForStudentList.ViewHolder2 holder, int position) {
        StudentsDetail studentsDetail = studentDetails.get(position);
        ArrayList<Dates> temp=studentsDetail.getDates();
        for(Dates d:temp){
            if(d.getYear()==date.getYear() && d.getDate()==date.getDate() && d.getMonth()==date.getMonth()){
                if(d.getAttendance()==1){
                    holder.studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext_green);
holder.selection=2;
                }else if(d.getAttendance()==-1){
                    holder.studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext_red);
                holder.selection=0;
                }else{
                    holder.studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext);
                holder.selection=1;
                }
            }
        }
        holder.student_name.setText(studentsDetail.getStudentName());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return studentDetails.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView student_name;
        public TextView enrollment_number;
        public ConstraintLayout studentItemLayout;
        int selection=1;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            studentItemLayout=itemView.findViewById(R.id.studentItem);
            student_name = itemView.findViewById(R.id.studentName);






            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println(v.getTag());
            StudentsDetail studentsDetail = studentDetails.get(Integer.parseInt(v.getTag().toString()));
            String currentDate="";

            if(date.getDate()<10){
                if(date.getMonth()<10){
                     currentDate="0"+date.getDate()+"-0"+ date.getMonth()+"-"+date.getYear();
                }else{
                     currentDate="0"+date.getDate()+"-"+ date.getMonth()+"-"+date.getYear();

                }
            }else if(date.getMonth()<10){
                 currentDate=date.getDate()+"-0"+ date.getMonth()+"-"+date.getYear();

            }else{
                 currentDate=date.getDate()+"-"+ date.getMonth()+"-"+date.getYear();
            }


            if (selection==1){
                studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext_green);
                databaseRef.child(USER_ID).child("CLASS").child(CLASS_ID).child("students").child(studentsDetail.getId()).child("DATES").child(currentDate).child("attendance").setValue(1);
                selection=2;
            }
            else if(selection==2){
                studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext_red);
                databaseRef.child(USER_ID).child("CLASS").child(CLASS_ID).child("students").child(studentsDetail.getId()).child("DATES").child(currentDate).child("attendance").setValue(-1);
                selection=0;
            }else if(selection==0){
                studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext);
                databaseRef.child(USER_ID).child("CLASS").child(CLASS_ID).child("students").child(studentsDetail.getId()).child("DATES").child(currentDate).child("attendance").setValue(0);
                selection=1;
            }

        }
    }


}
