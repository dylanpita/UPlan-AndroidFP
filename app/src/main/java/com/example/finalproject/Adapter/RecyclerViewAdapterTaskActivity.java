package com.example.finalproject.Adapter;

import android.content.Context;
import android.os.Bundle;
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

public class RecyclerViewAdapterTaskActivity extends RecyclerView.Adapter<RecyclerViewAdapterTaskActivity.ViewHolder> {

    private Context context;
    private List<Task> taskList;

    public RecyclerViewAdapterTaskActivity(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row_activity,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.name.setText(task.getName());
        holder.description.setText(task.getDescription());
        holder.endTime.setText(task.getEnd_date());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView description;
        private TextView endTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.task_name);
            description = itemView.findViewById(R.id.task_description);
            endTime = itemView.findViewById(R.id.task_end_time);
        }

        int getId(int position) {
            return taskList.get(position).getId();
        }

        @Override
        public void onClick(View v) {
            int position = getId(getAdapterPosition());

            Bundle bundle = new Bundle();
            bundle.putInt("id", position);
            CreateTaskFragment createTaskFragment = new CreateTaskFragment();
            createTaskFragment.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().add(R.id.frame, createTaskFragment).commit();
        }
    }
}
