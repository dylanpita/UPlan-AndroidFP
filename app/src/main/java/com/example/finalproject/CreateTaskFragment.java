package com.example.finalproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.finalproject.Data.DatabaseHandler;
import com.example.finalproject.Model.Task;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.Calendar;
import java.util.List;

public class CreateTaskFragment extends Fragment implements View.OnClickListener, Validator.ValidationListener {

    public CreateTaskFragment() {
        // Required empty public constructor
    }
    @NotEmpty
    private EditText name, description,endDate,endTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Button submitBtn;

    private Validator validator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        validator = new Validator(this);
        validator.setValidationListener(this);
        
        endDate = (EditText) view.findViewById(R.id.edit_task_endDate);
        endTime = (EditText) view.findViewById(R.id.edit_task_endTime);
        
        endDate.setOnClickListener(this);
        endTime.setOnClickListener(this);

        submitBtn = (Button) view.findViewById(R.id.submit_button);

        submitBtn.setOnClickListener(this);

        name = (EditText) view.findViewById(R.id.edit_task_name);
        description = (EditText) view.findViewById(R.id.edit_task_description);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey("id")) {

                int id = bundle.getInt("id");

                final DatabaseHandler db = new DatabaseHandler(getContext());

                Task task = db.getTask(id);
                
                String[] inputEndDate = task.getEnd_date().split(" ");

                name.setText(task.getName());
                description.setText(task.getDescription());
                endDate.setText(inputEndDate[0]);
                endTime.setText(inputEndDate[1]);
            }
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        
        if (view == endDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            endDate.setText(year + "-"
                                    + String.format("%02d",(monthOfYear + 1)) + "-"
                                    + String.format("%02d", dayOfMonth));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == endTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            endTime.setText(hourOfDay + ":" + String.format("%02d",minute));
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        if (view == submitBtn) {
            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(getContext(), "Task submitted, thanks!", Toast.LENGTH_SHORT).show();

        String t_name = String.valueOf(name.getText());
        String t_desc = String.valueOf(description.getText());
        String t_end = endDate.getText() + " " + endTime.getText();
        String t_entry = (mYear+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute);
        int step = 1;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        final DatabaseHandler db = new DatabaseHandler(getContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey("id")) {
                int id = getArguments().getInt("id");
                Task task = new Task(id, t_name, t_desc, t_entry, t_end, step);
                db.updateTask(task);

                getArguments().clear();
            }
        }else {
            Task task = new Task(t_name, t_desc, t_entry, t_end, step);
            db.addTask(task);
        }
        getActivity().recreate();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(getContext(), "Please fix the errors above", Toast.LENGTH_SHORT).show();
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
