package com.oasis.todo;
// TaskAdapter.java
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_task, parent, false);
        }

        TextView textViewTask = convertView.findViewById(R.id.textViewTask);
        CheckBox checkBoxDone = convertView.findViewById(R.id.checkBoxTask);

        textViewTask.setText(task.getTaskName());
        checkBoxDone.setChecked(task.isChecked());

        if (task.isChecked()) {
            textViewTask.setPaintFlags(textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textViewTask.setPaintFlags(textViewTask.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        checkBoxDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setChecked(isChecked);
            if (isChecked) {
                textViewTask.setPaintFlags(textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textViewTask.setPaintFlags(textViewTask.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        return convertView;
    }
}
