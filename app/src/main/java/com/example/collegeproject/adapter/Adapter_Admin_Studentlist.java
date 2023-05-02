package com.example.collegeproject.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.collegeproject.R;
import com.example.collegeproject.model.Student;
import com.example.collegeproject.model.Task_Diff_call;

import java.util.ArrayList;

public class Adapter_Admin_Studentlist extends RecyclerView.Adapter<Adapter_Admin_Studentlist.viewHolder> {


    private ArrayList<Student> studentArrayList=new ArrayList<>();
    private Context context;

    private ItemClcikListener clcikListener;

    public Adapter_Admin_Studentlist(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_admin_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Student student=studentArrayList.get(position);
        holder.student_view.setText(student.getstudentdetail());
        Glide.with(context).load(student.getImage_url()).into(holder.student_image);

    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }


    public void setClcikListener(ItemClcikListener clcikListener) {
        this.clcikListener = clcikListener;
    }



    public  class  viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView student_image;
        private TextView student_view;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            student_view=itemView.findViewById(R.id.student_admin_details);
            student_image=itemView.findViewById(R.id.student_admin_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
if(clcikListener !=null){
clcikListener.onClick(v,getAdapterPosition());
}
        }
    }

    public interface ItemClcikListener{
        void onClick(View view,int postion);
    }

    public void setstudent(ArrayList<Student> newlisttask) {
//        this.listtask = listtask;
//        notifyDataSetChanged();
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff
                (new Task_Diff_call(studentArrayList,newlisttask),false);
        studentArrayList = newlisttask;
        result.dispatchUpdatesTo(Adapter_Admin_Studentlist.this);
   notifyDataSetChanged();
    }

}
