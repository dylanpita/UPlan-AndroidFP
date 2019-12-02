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

import com.example.finalproject.CreateEventFragment;
import com.example.finalproject.Model.Event;
import com.example.finalproject.R;

import java.util.List;

public class RecyclerViewAdapterEvent extends RecyclerView.Adapter<RecyclerViewAdapterEvent.ViewHolder> {

    private Context context;
    private List<Event> eventList;

    public RecyclerViewAdapterEvent(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.name.setText(event.getName());
        holder.startTime.setText(event.getStart_date());
        holder.endTime.setText(event.getEnd_date());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView startTime;
        private TextView endTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.event_name);
            startTime = itemView.findViewById(R.id.event_start_time);
            endTime = itemView.findViewById(R.id.event_end_time);
        }

        int getId(int position) {
            return eventList.get(position).getId();
        }

            @Override
        public void onClick(View v) {
            int position = getId(getAdapterPosition());

            Log.d("Debug", "onClick: Getting inside onClick");

            Bundle bundle = new Bundle();
            bundle.putInt("id", position);
            CreateEventFragment createEventFragment = new CreateEventFragment();
            createEventFragment.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().add(R.id.frame, createEventFragment).commit();
        }
    }
}
