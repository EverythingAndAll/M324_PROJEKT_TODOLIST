package com.example.demo;

/**
 * Das hier ist die weiterentwickelte Version 2 unseres Task-Modells.
 * Wird eigentlich nur fuer das Versions-Beispiel genutzt, um zu zeigen, dass sich das Datenmodell aendern kann.
 */
public class TaskV2 {
    private String taskdescription;
    private int priority; // <-- Das ist das neue Feld in v2 (z.B. 1=Hoch, 2=Mittel)

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
