package com.oasis.todo;
// Task.java
public class Task {
    public int task;
    public boolean check;
    private long id;
    private String name;
    private boolean checked;

    public Task(long id, String name, boolean checked) {
        this.id = id;
        this.name = name;
        this.checked = checked;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getTaskName() {
        return task;
    }
}
