package com.example.demo;

/** 
 * Das ist unser erweitertes Task-Modell fuer die ToDo-Liste.
 * Beinhaltet ID, Beschreibung, Termine, Priorität und Abschluss-Status.
 */
public class Task {
	
	private Long id;
	private String taskdescription;
	private String creationDate;
	private String dueDate;
	private String priority; // "HIGH", "MEDIUM", "LOW"
	private boolean completed;

	public Task() {
		this.creationDate = java.time.LocalDateTime.now().toString();
		this.completed = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskdescription() { 
		return taskdescription;
	}

	public void setTaskdescription(String taskdescription) { 
		this.taskdescription = taskdescription;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}