package com.example.demo;

/** 
 * Das ist unser super simples Task-Modell fuer die ToDo-Liste.
 * 
 * @author luh (Kommentare aufgeraeumt)
 */
public class Task {
	
	private String taskdescription; // Wichtig: Der Name muss exakt mit dem React-State uebereinstimmen, sonst zickt das Mapping!
	private String creationDate;

	public Task() {
        this.creationDate = java.time.LocalDateTime.now().toString();
    }

	public String getTaskdescription() { // Achtung: Hier kein CamelCase erzwingen, da es sonst Probleme mit dem Jackson-Bean-Mapping geben kann
		return taskdescription;
	}

	public void setTaskdescription(String taskdescription) { // Gleiches Spiel nochmal: Kein extra CamelCase fuer die Bean!
		this.taskdescription = taskdescription;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

}