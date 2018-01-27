package com.brightapps.all;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.brightapps.all.mDatabase.DateTime;
import com.brightapps.all.mDatabase.MainDbAdapter;
import com.brightapps.all.mRecycler.TodoRCAdapter;

import java.text.ParseException;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by kyadamakanti on 12/25/2017.
 */

public class TodoDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "KIT-TodoDialogFrag";

    MainDbAdapter myAdapter;
    private DatePickerDialog fromDatePickerDialog;
    String dueDate;
    Calendar newCalendar;
    int position;
    EditText editTitle;
    EditText editDescription;
    TextView editDate;
    TextView editTime;
    Spinner spinner;
    public ToDoDialogListner toDoDialogListner;

    public TodoDialogFragment() {
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        position = args.getInt(Util.TASKPOSITION);
        String todoTitle = args.getString(Util.TASKTITILE);
        String todoDescription = args.getString(Util.TASKDESC);
        String todoDateTime = args.getString(Util.TASKDATE);
        int reccurance = args.getInt(Util.TASKRECCURANCE);

        Log.d(TAG, "onCreateDialog: Title : "+ todoTitle);
        Log.d(TAG, "onCreateDialog: Position for Add/edit record : " + position);
        Log.d(TAG, "onCreateDialog: DateTime : " +todoDateTime );
        Log.d(TAG,"onCreatDialog : Reccurance  "  + reccurance);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Task");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.todo_dialog_layout, null);


        editTitle =  dialogView.findViewById(R.id.todoEditTitle);
        editDescription = dialogView.findViewById(R.id.todoEditDesc);
        editDate =  dialogView.findViewById(R.id.todoEditDate);
        editTime = dialogView.findViewById(R.id.todoEditTime);
        spinner = (Spinner) dialogView.findViewById(R.id.reccurent_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Alarm_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        newCalendar = Calendar.getInstance();
        DateTime dateTime;
        int year;
        int month;
        int day;
        int hour;
        int minute;


        dateTime = Util.getTimeValues(todoDateTime);
        year = dateTime.getYear();
        month = dateTime.getMonth();
        day = dateTime.getDay();
        hour = dateTime.getHour();
        minute = dateTime.getMinute();
        String amPM = dateTime.getAmPM();


        if (position > -1) {
            //Edit record
            builder.setTitle("Edit Task");

            editTitle.setText(todoTitle);
            editDescription.setText(todoDescription);
            spinner.setSelection(reccurance);
        }
        else {
            spinner.setSelection(0);
        }
        editDate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
        editTime.setText(new StringBuilder().append(hour).append(":").append(minute).append(" ").append(amPM));


        //On click event for Date Text View date in Dialog

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //Log.d("KITDBG", "On click event in edit box");

                fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        editDate.setText(new StringBuilder()
                                .append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
                        // Log.d("KITDBG", "selected Date in edit box : " + dueDate);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
            }
        });

        //On click event for Edit Time in Dialog

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                int currentHour;
                                String aMpM = "AM";

                                if (hourOfDay > 11) {
                                    currentHour = hourOfDay - 12;
                                    aMpM = "PM";
                                } else {
                                    currentHour = hourOfDay;
                                }

                                editTime.setText(currentHour + ":" + minute + " " + aMpM);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        builder.setView(dialogView);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Add Todomain Record

                Log.d(TAG, "onClick: On Save Button the Task Record");
                Log.d(TAG, "Save Dialog: position : " + position);
                Log.d(TAG, "Save Dialog: Title : " + editTitle.getText());
                Log.d(TAG, "Save Dialog: Description : " + editDescription.getText());
                Log.d(TAG, "Save Dialog:  Date : " + editDate.getText());
                Log.d(TAG, "Save Dialog:  Time : " + editTime.getText());
                Log.d(TAG, "Save Dialog:  Reccurance " + spinner.getSelectedItem().toString());


                if (position >= 0) {

                    toDoDialogListner.editRecord(dialogView, position);

                } else {
                    toDoDialogListner.addRecord(dialogView, position);

                }

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Go Back to main activity
                TodoDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();

    }

    public void settodoListner(TodoDialogFragment.ToDoDialogListner todoListner) {
        this.toDoDialogListner = todoListner;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Spinner Selection
        String reccurance = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface ToDoDialogListner {
        public void addRecord(View view, int position);

        public void editRecord(View view, int position);
    }


}
