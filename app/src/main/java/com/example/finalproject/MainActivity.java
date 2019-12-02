package com.example.finalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Adapter.RecyclerViewAdapterEvent;
import com.example.finalproject.Adapter.RecyclerViewAdapterTask;
import com.example.finalproject.Data.DatabaseHandler;
import com.example.finalproject.Model.Event;
import com.example.finalproject.Model.Task;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private DrawerLayout drawerLayout;

    private ArrayList<Event> eventArrayList;
    private RecyclerViewAdapterEvent recyclerViewAdapterEvent;
    private RecyclerView recyclerViewEvent;

    private ArrayList<Task> taskArrayList;
    private RecyclerViewAdapterTask recyclerViewAdapterTask;
    private RecyclerView recyclerViewTask;

    private CalendarView calendarView;

    private ImageButton addEventBtn;
    private ImageButton addTaskBtn;

    private PendingIntent pendingIntent;

    public void refresh() {
        setContentView(R.layout.activity_main);
        layout = (LinearLayout) findViewById(R.id.layout);
        configureNavigationDrawer();
        configureToolbar();

        eventArrayList = new ArrayList<>();
        taskArrayList = new ArrayList<>();

        final DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("Debug", "refresh: "+date);
        List<Event> eventList = db.getDayEvents(date);
        for (Event event : eventList) {
            eventArrayList.add(event);
        }
        List<Task> taskList = db.getDayTasks(date);
        for (Task task : taskList) {
            taskArrayList.add(task);
        }

        recyclerViewEvent = findViewById(R.id.events_recyclerview);
        recyclerViewEvent.setHasFixedSize(true);
        recyclerViewEvent.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerViewAdapterEvent = new RecyclerViewAdapterEvent(MainActivity.this, eventArrayList);

        recyclerViewEvent.setAdapter(recyclerViewAdapterEvent);

        recyclerViewTask = findViewById(R.id.tasks_recyclerview);
        recyclerViewTask.setHasFixedSize(true);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerViewAdapterTask = new RecyclerViewAdapterTask(MainActivity.this, taskArrayList);

        recyclerViewTask.setAdapter(recyclerViewAdapterTask);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                String date = year+"-"+String.format("%02d", month)+"-"+String.format("%02d", dayOfMonth);
                Log.d("Debug", "onSelectedDayChange: "+date);
                List<Event> eventList = db.getDayEvents(date);
                eventArrayList = new ArrayList<>();
                eventArrayList.addAll(eventList);

                recyclerViewAdapterEvent = new RecyclerViewAdapterEvent(MainActivity.this, eventArrayList);

                recyclerViewEvent.setAdapter(recyclerViewAdapterEvent);

                List<Task> taskList = db.getDayTasks(date);
                taskArrayList = new ArrayList<>();
                taskArrayList.addAll(taskList);

                recyclerViewAdapterTask = new RecyclerViewAdapterTask(MainActivity.this, taskArrayList);

                recyclerViewTask.setAdapter(recyclerViewAdapterTask);
            }
        });

        addEventBtn = findViewById(R.id.add_event_btn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, new CreateEventFragment());
                ft.commit();
            }
        });
        addTaskBtn = findViewById(R.id.add_task_btn);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, new CreateTaskFragment());
                ft.commit();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh();
        alarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }
    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.drawer);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }
    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment f = null;
                int itemId = menuItem.getItemId();
                if (itemId == R.id.calendar) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.workboard) {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.events) {
                    Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.tasks) {
                    Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.settings) {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                }
                if (f != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, f);
                    transaction.commit();
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            // Android home
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menuitem_settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                break;
            // manage other entries if you have it ...
        }
        return true;
    }

    public void alarm(){

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /* Set the alarm to start at 8 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);

        /* Repeating on every day interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                60000*60*12, pendingIntent);
    }
}