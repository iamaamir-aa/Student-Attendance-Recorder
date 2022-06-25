package com.android.studentattendancerecorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        holder.enrollment_number.setText(studentsDetail.getEnrollmentNumber());
        holder.student_name.setText(studentsDetail.getStudentName());

    }

    @Override
    public int getItemCount() {
        return studentDetails.size();
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView student_name;
        public TextView enrollment_number;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            student_name = itemView.findViewById(R.id.studentName);
            enrollment_number = itemView.findViewById(R.id.enrollmentNumber);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "O BHAI MOJ KRDI", Toast.LENGTH_SHORT).show();

        }
    }


}
