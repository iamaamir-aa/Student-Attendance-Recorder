package com.android.studentattendancerecorder.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.studentattendancerecorder.FirstFragment;
import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.R;
import com.android.studentattendancerecorder.SecondFragment;
import com.android.studentattendancerecorder.SecondFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapterForClassList extends RecyclerView.Adapter<RecyclerViewAdapterForClassList.ViewHolder> {
    private Context context;
    private List<ClassAndSubjectDetails> classAndSubjectDetailsList;
    private Fragment fragment;

    public RecyclerViewAdapterForClassList(Context context, List<ClassAndSubjectDetails> classAndSubjectDetailsList, Fragment frag) {
        this.context = context;
        this.classAndSubjectDetailsList = classAndSubjectDetailsList;
        fragment=frag;
    }
//create card/item/holder
    @NonNull
    @Override
    public RecyclerViewAdapterForClassList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.classlistitem,parent,false);
        return new ViewHolder(view);
    }

    //set details of each holder/card/item
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterForClassList.ViewHolder holder, int position) {
        ClassAndSubjectDetails classAndSubjectDetails=classAndSubjectDetailsList.get(position);
holder.class_name.setText(classAndSubjectDetails.getClass_name());
        holder.subject_name.setText(classAndSubjectDetails.getSubject_name());

    }

    @Override
    public int getItemCount() {
        return classAndSubjectDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public TextView class_name;
        public TextView subject_name;        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            class_name=itemView.findViewById(R.id.className);

            subject_name=itemView.findViewById(R.id.subjectName);
        }

        @Override
        public void onClick(View v) {
            Log.d("ERT","70");
            SecondFragmentDirections.ActionSecondFragmentToStudentListFragment action=SecondFragmentDirections.actionSecondFragmentToStudentListFragment();
            action.setClassID(classAndSubjectDetailsList.get(getLayoutPosition()).getId());
            NavHostFragment.findNavController(fragment).navigate(action);

        }

        @Override
        public boolean onLongClick(View v) {



            return false;
        }
    }
}
