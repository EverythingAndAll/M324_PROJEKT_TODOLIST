package com.example.demo;

public class TaskV2 {
    private String taskdescription;
    private int priority; // <-- Das ist das neue Feld in Version 2!

    public TaskV2(String taskdescription, int priority) {
        this.taskdescription = taskdescription;
        this.priority = priority;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
