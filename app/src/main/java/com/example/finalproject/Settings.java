package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.Data.DatabaseHandler;
import com.example.finalproject.Model.Event;
import com.example.finalproject.Model.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    private LinearLayout layout;
    private DrawerLayout drawerLayout;

    private Button del_t_all, del_e_all, del_e, del_t;
    private Spinner choice_t, choice_e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        layout = (LinearLayout) findViewById(R.id.layout);
        configureNavigationDrawer();
        configureToolbar();

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        final List<Task> taskList = db.getTasks();
        
        choice_t = findViewById(R.id.del_task_spinner);
        List<String> list = new ArrayList<String>();

        for (Task task : taskList) {
            list.add(task.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choice_t.setAdapter(dataAdapter);

        final List<Event> eventList = db.getEvents();

        choice_e = findViewById(R.id.del_event_spinner);
        List<String> list2 = new ArrayList<String>();

        for (Event event : eventList) {
            list2.add(event.getName());
        }

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choice_e.setAdapter(dataAdapter2);

        del_t = findViewById(R.id.del_task);

        del_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = taskList.get(choice_t.getSelectedItemPosition());
                db.deleteTask(task.getId());
                recreate();
            }
        });

        del_e = findViewById(R.id.del_event);
        del_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = eventList.get(choice_e.getSelectedItemPosition());
                db.deleteEvent(event.getId());
                recreate();
            }
        });
        del_e_all = findViewById(R.id.del_all_event);
        del_e_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAllEvents();
                recreate();
            }
        });
        del_t_all = findViewById(R.id.del_all_task);
        del_t_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAllTasks();
                recreate();
            }
        });
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
