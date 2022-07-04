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
import com.android.studentattendancerecorder.Model.StudentsDetail;
import com.android.studentattendancerecorder.R;
import com.android.studentattendancerecorder.SecondFragmentDirections;

import java.util.List;

public class RecyclerViewAdapterForStudentList extends RecyclerView.Adapter<RecyclerViewAdapterForStudentList.ViewHolder2> {
    private Context context;
    private List<StudentsDetail> studentDetails;
    private Fragment fragment;



    public RecyclerViewAdapterForStudentList(Context context, List<StudentsDetail> studentDetails, Fragment fragment) {
        this.context = context;
        this.studentDetails = studentDetails;
        this.fragment = fragment;
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
        //holder.enrollment_number.setText(studentsDetail.getEnrollmentNumber());
        holder.student_name.setText(studentsDetail.getStudentName());

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
            if (selection==1){
                studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext_green);
                selection=2;
            }
            else if(selection==2){
                studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext_red);
                selection=0;
            }else if(selection==0){
                studentItemLayout.setBackgroundResource(R.drawable.rounded_edittext);
selection=1;
            }

        }
    }


}
