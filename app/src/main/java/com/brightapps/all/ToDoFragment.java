package com.brightapps.all;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brightapps.all.mDatabase.MainDbAdapter;
import com.brightapps.all.mDatabase.TodoObject;
import com.brightapps.all.mRecycler.RecyclerItemTouchHelper;
import com.brightapps.all.mRecycler.TodoRCAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoFragment extends Fragment implements TodoRCAdapter.ClickListner, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, TodoDialogFragment.ToDoDialogListner {
    private static final String TAG = "KIT-ToDoFragment";

    MainDbAdapter myDB;
    TodoRCAdapter rcAdapter;
    Cursor cursor;
    ArrayList<TodoObject> todos;
    RelativeLayout layout;
    static View.OnClickListener myOnClickListener;

    public ToDoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_todo, container, false);
        layout = v.findViewById(R.id.relativeLayoutID);

        todos = new ArrayList<>();
        getAllTasks();

        RecyclerView recyclerView = v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        rcAdapter = new TodoRCAdapter(getContext(), todos);
        rcAdapter.setClickListener(this);
        recyclerView.setAdapter(rcAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Clicked on Add Task Button");
                showAlertDialog(view, -1);
            }
        });
        return v;
    }


    private void showAlertDialog(View view, int position) {
        TodoDialogFragment dialog = new TodoDialogFragment();
        dialog.settodoListner(this);
        Bundle args = new Bundle();
        args.putInt("TASKPOSITION", position);

        if (position >= 0) {

            // Create an instance of the dialog fragment and show it
            TextView textViewTitle = view.findViewById(R.id.textViewTitle);
  //          TextView textViewDescription = view.findViewById(R.id.textViewDesc);
    //        TextView textViewDate = view.findViewById(R.id.textViewDate);

            Log.d(TAG, "showAlertDialog textViewTitle : " + textViewTitle.getText().toString());
            Log.d(TAG, "showAlertDialog textViewDescription : " + todos.get(position).getDescription());
            Log.d(TAG, "showAlertDialog textViewDate : " + todos.get(position).getReminder_Date());

            args.putString(Util.TASKTITILE, textViewTitle.getText().toString());
            args.putString(Util.TASKDESC, todos.get(position).getDescription());
            args.putString(Util.TASKDATE, todos.get(position).getReminder_Date());
            args.putInt(Util.TASKRECCURANCE, todos.get(position).getRecurrence());

        }
        dialog.setArguments(args);

        dialog.show(getActivity().getSupportFragmentManager(), "TodoDialogFragment");

    }

    @Override
    public void itemClicked(View view, int position) {
        //Edit clicked on Recycler view
        Log.d(TAG, "itemClicked: position : " + position);
        Log.d(TAG, "itemClicked : Row id for the position : " + todos.get(position).getRow_id());

        showAlertDialog(view, position);


    }


    private void getAllTasks() {
        myDB = new MainDbAdapter(getActivity());
        myDB.open();
        cursor = myDB.getAllRows();

        while (cursor.moveToNext()) {
            TodoObject todo = new TodoObject();
            todo.setRow_id(cursor.getInt(cursor.getColumnIndex(MainDbAdapter.KEY_ROWID)));

            todo.setTitle(cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_TITLE)));
            todo.setDescription(cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_DESCRIPTION)));
            todo.setReminder_Date(cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_REMINDER_DATE)));
            todo.setCategory_name(cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_CATEGORY_NAME)));
            todo.setProirity(cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_PRIORITY)));
            todo.setRecurrence(cursor.getInt(cursor.getColumnIndex(MainDbAdapter.KEY_RECURRENCE)));
            Log.d(TAG, "getAllTasks: Title :" + cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_TITLE)));
            Log.d(TAG, "getAllTasks: Reccurance : " + cursor.getInt(cursor.getColumnIndex(MainDbAdapter.KEY_RECURRENCE)));
            Log.d(TAG, "getAllTasks: Time " + cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_REMINDER_DATE)));
            todo.setLocation(cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_LOCATION)));
            todo.setTask_Completion_status(cursor.getInt(cursor.getColumnIndex(MainDbAdapter.KEY_TASK_COMPLTETION_STATUS)));
            todos.add(todo);
        }
    }

    @Override
    public void addRecord(View dialogView, int position) {
        EditText editTitle = dialogView.findViewById(R.id.todoEditTitle);
        EditText editdescription = dialogView.findViewById(R.id.todoEditDesc);
        TextView editdate = dialogView.findViewById(R.id.todoEditDate);
        TextView edittime = dialogView.findViewById(R.id.todoEditTime);
        Spinner spinner = dialogView.findViewById(R.id.reccurent_spinner);

        String dateTime = editdate.getText().toString() + " " + edittime.getText().toString();
        int reccurance_id = Util.getReccuranceId(spinner.getSelectedItem().toString());

        //If the Title is empty while saving
        if (editTitle.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Information!");
            builder.setMessage("Title cannot be null");
            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getActivity(),"Get Started!",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            AlertDialog diag = builder.create();
            diag.show();
        }

        Log.d(TAG, "addRecord: Title : " + editTitle.getText().toString());
        Log.d(TAG, "addRecord : Date Time " + dateTime);
        Log.d(TAG, "addRecord : Reccurance ID " + reccurance_id);


        long row_id = myDB.insertRow(editTitle.getText().toString(), editdescription.getText().toString(), dateTime,
                Util.SNOOZE_AMOUNT, Util.NULL_TEXT, Util.CATEGORY_NAME, reccurance_id,
                Util.NULL_TEXT, Util.TASK_ACTIVE);
        if (row_id > 0) {
            TodoObject todo = new TodoObject();
            todo.setTitle(editTitle.getText().toString());
            todo.setDescription(editdescription.getText().toString());
            todo.setReminder_Date(dateTime);
            todo.setRecurrence(reccurance_id);
            todos.add(todo);
            rcAdapter.notifyItemInserted(todos.size());
            Util.scheduleNotification(getContext(),dateTime, editTitle.getText().toString(), "1", ((int) row_id), 0, reccurance_id);

        }

    }

    @Override
    public void editRecord(View dialogView, int position) {
        EditText editTitle = dialogView.findViewById(R.id.todoEditTitle);
        EditText editdescription = dialogView.findViewById(R.id.todoEditDesc);
        TextView editdate = dialogView.findViewById(R.id.todoEditDate);
        TextView edittime = dialogView.findViewById(R.id.todoEditTime);
        Spinner spinner = dialogView.findViewById(R.id.reccurent_spinner);

        String dateTime = editdate.getText().toString() + " " + edittime.getText().toString();
        int reccurance_id = Util.getReccuranceId(spinner.getSelectedItem().toString());

        Log.d("KITDBG", "In Edit Record with row id : " + todos.get(position).getRow_id() + " and position : " + position);
        Log.d("KITDBG-" + TAG, "addRecord : Reccurance ID " + reccurance_id);


        boolean updated = myDB.updateRow(todos.get(position).getRow_id(), editTitle.getText().toString(), editdescription.getText().toString(), dateTime
                , Util.SNOOZE_AMOUNT, Util.NULL_TEXT, Util.CATEGORY_NAME, reccurance_id, Util.NULL_TEXT, Util.TASK_ACTIVE);
        if (updated) {
            todos.get(position).setTitle(editTitle.getText().toString());
            todos.get(position).setDescription(editdescription.getText().toString());
            todos.get(position).setReminder_Date(dateTime);
            todos.get(position).setRecurrence(reccurance_id);
            rcAdapter.notifyItemChanged(position);
            Util.scheduleNotification(getContext(),dateTime, editTitle.getText().toString(), "1", ((int) todos.get(position).getRow_id()), 0, reccurance_id);

        }
    }

        //Delete the Task
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TodoRCAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar

            final int deltedIndex = viewHolder.getAdapterPosition();
            Log.d(TAG, "onSwiped: deleted Index : " + deltedIndex + " RowId : " + todos.get(deltedIndex).getRow_id() );
            boolean deleted = myDB.deleteRow(todos.get(deltedIndex).getRow_id());
            if (deleted) {
                Util.scheduleNotification(getContext(),todos.get(deltedIndex).getReminder_Date(), todos.get(deltedIndex).getTitle(), "1", ((int) todos.get(position).getRow_id()), 0, 0);
                todos.remove(deltedIndex);
                rcAdapter.notifyItemRemoved(deltedIndex);
                Snackbar.make(layout, "Task Deleted", Snackbar.LENGTH_LONG).show();
            }

        }
    }
}
