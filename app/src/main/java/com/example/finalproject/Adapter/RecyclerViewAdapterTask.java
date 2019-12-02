package com.example.finalproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.CreateTaskFragment;
import com.example.finalproject.Model.Task;
import com.example.finalproject.R;

import java.util.List;

public class RecyclerViewAdapterTask extends RecyclerView.Adapter<RecyclerViewAdapterTask.ViewHolder> {

    private Context context;
    private List<Task> taskList;

    public RecyclerViewAdapterTask(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterTask.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.name.setText(task.getName());
        holder.dueTime.setText(task.getEnd_date());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView name;
            private TextView dueTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                itemView.setOnClickListener(this);

                name = itemView.findViewById(R.id.task_name);
                dueTime = itemView.findViewById(R.id.task_due_time);
            }

        int getId(int position) {
            return taskList.get(position).getId();
        }

        @Override
        public void onClick(View v) {
            int position = getId(getAdapterPosition());

            Log.d("Debug", "onClick: Getting inside onClick");

            Bundle bundle = new Bundle();
            bundle.putInt("id", position);
            CreateTaskFragment createTaskFragment = new CreateTaskFragment();
            createTaskFragment.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().add(R.id.frame, createTaskFragment).commit();
        }
    }
}
