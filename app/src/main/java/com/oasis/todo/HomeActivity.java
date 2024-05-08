package com.oasis.todo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<String> tasks;
    private ArrayAdapter<String> adapter;
    private EditText editTextTask;

    // Get the list view from list_view_task
    private DBHelper dbHelper;

    private Calendar calendar;
    private int year, month, day, hour, minute;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DBHelper(this);
        editTextTask = findViewById(R.id.editTextNote);
        ///signout button
        Button signout = findViewById(R.id.signOutButton);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to MainActivity
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tasks = new ArrayList<>();
        ListView listViewTasks = findViewById(R.id.listViewTasks);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_task, R.id.textViewTask, tasks) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBoxTask = view.findViewById(R.id.checkBoxTask);
                checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // when checkbox is checked the task is striked through
                    if (isChecked) {
                        view.setAlpha(0.5f);
                        dbHelper.markTaskAsCompleted(getTaskId());
                    } else {
                        view.setAlpha(1.0f);
                        dbHelper.markTaskAsCompleted(getTaskId());
                    }

                    // You can perform actions like marking the task as completed or not
                });
                return view;
            }
        };
        listViewTasks.setAdapter(adapter);

        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteDialog();
            }
        });

        // Load tasks from the database when the activity starts
        loadTasks();
    }



    private void showNoteDialog() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.dialog_add_task);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        EditText editTextNote = dialog.findViewById(R.id.editTextNote);
        Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextNote.getText().toString().trim();
                if (!note.isEmpty()) {
                    long taskId = dbHelper.addTask(note, year, month, day, hour, minute);
                    tasks.add(note);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(HomeActivity.this, "Please enter a note", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Load tasks from the database
    private void loadTasks() {
        tasks.clear();
        Cursor cursor = dbHelper.getAllTasksForUser(1L);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String taskName = cursor.getString(cursor.getColumnIndexOrThrow("TASK_NAME"));
                tasks.add(taskName);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }
}
