package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Adapter.RecyclerViewAdapterTaskActivity;
import com.example.finalproject.Data.DatabaseHandler;
import com.example.finalproject.Model.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private LinearLayout layout;
    private DrawerLayout drawerLayout;

    private ArrayList<Task> taskArrayList;
    private RecyclerViewAdapterTaskActivity recyclerViewAdapterTaskActivity;
    private RecyclerView recyclerViewTaskActivity;

    private ImageButton addTaskBtn;

    private void refresh() {
        setContentView(R.layout.activity_task);
        layout = (LinearLayout) findViewById(R.id.layout);
        configureNavigationDrawer();
        configureToolbar();

        taskArrayList = new ArrayList<>();

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        List<Task> taskList = db.getAllTasks();
        for (Task task : taskList) {
            taskArrayList.add(task);
        }

        recyclerViewTaskActivity = findViewById(R.id.tasks_recyclerview_activity);
        recyclerViewTaskActivity.setHasFixedSize(true);
        recyclerViewTaskActivity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerViewAdapterTaskActivity = new RecyclerViewAdapterTaskActivity(getApplicationContext(), taskArrayList);

        recyclerViewTaskActivity.setAdapter(recyclerViewAdapterTaskActivity);

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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onCreate(new Bundle());
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
}
