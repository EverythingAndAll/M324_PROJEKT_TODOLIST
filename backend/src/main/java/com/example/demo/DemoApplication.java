package com.example.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Das hier ist unsere kleine Demo-Anwendung. Im Grunde ist das unser REST-Controller fuer die ToDo-Liste.
 * Da wir keine Datenbank angebunden haben, speichern wir die Tasks einfach zur Laufzeit in einer lokalen Liste ab.
 * 
 * Kurzer Ueberblick ueber unsere Endpunkte:
 * - "/" gibt uns einfach die vollstaendige Liste der Tasks zurueck.
 * - "/tasks" fügt einen neuen (einzigartigen) Task hinzu.
 * - "/delete" wirft einen Task anhand seiner Beschreibung wieder aus der Liste.
 * 
 * Die Daten schickt uns das (React-)Frontend als JSON im Request-Body rueber.
 * Mit Jackson wandeln wir dieses JSON dann super bequem direkt in Java-Objekte (Task.java) um.
 * Ausserdem habe ich ueberall @CrossOrigin drangeschrieben, damit der Browser bei den Frontend-Requests nicht mit CORS-Fehlern nervt.
 *
 * @author luh (Kommentare aufgeraeumt)
 */
@RestController
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private List<Task> tasks = new ArrayList<>();
	private final ObjectMapper mapper = new ObjectMapper();

	@CrossOrigin
	@GetMapping("/")
	public List<Task> getTasks() {

		System.out.println("API Endpoint '/' wurde aufgerufen. Wir liefern aktuell " + tasks.size() + " Tasks zurueck.");
		if (tasks.size() > 0) {
			int i = 1;
			for (Task task : tasks) {
				System.out.println("- Task " + (i++) + ": " + task.getTaskdescription());
			}
		}
		return tasks; // Spring Boot baut aus unserer Liste automatisch ein JSON, was total praktisch ist!
	}

	@CrossOrigin
	@PostMapping("/tasks")
	public String addTask(@RequestBody String taskdescription) {
		System.out.println("API Endpoint '/tasks' aufgerufen mit Inhalt: '" + taskdescription + "'");
		try {
			Task task;
			// Hier parsen wir den reinkommenden String in ein verwertbares Task-Objekt um
			task = mapper.readValue(taskdescription, Task.class);
			for (Task t : tasks) {
				if (t.getTaskdescription().equals(task.getTaskdescription())) {
					System.out.println(">>> Info: Task '" + task.getTaskdescription() + "' gibt es leider schon!");
					return "redirect:/"; // Wir ignorieren Duplikate einfach mal...
				}
			}
			System.out.println("... fuege neuen Task hinzu: '" + task.getTaskdescription() + "'");
			tasks.add(task);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	@CrossOrigin
	@PostMapping("/delete")
	public String delTask(@RequestBody String taskdescription) {
		System.out.println("API Endpoint '/delete' aufgerufen, Ziel: '" + taskdescription + "'");
		try {
			Task task;
			task = mapper.readValue(taskdescription, Task.class);
			Iterator<Task> it = tasks.iterator();
			while (it.hasNext()) {
				Task t = it.next();
				if (t.getTaskdescription().equals(task.getTaskdescription())) {
					System.out.println("... loesche Task: '" + task.getTaskdescription() + "' erfolgreich.");
					it.remove(); // Sicher ueber den Iterator loeschen wegen ConcurrentModificationExceptions
					return "redirect:/";
				}
			}
			System.out.println(">>> Ups, Task: '" + task.getTaskdescription() + "' wurde in der Liste gar nicht gefunden!");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	@CrossOrigin
	@PostMapping("/clear")
	public String clearTasks() {
		System.out.println("API Endpoint '/clear' aufgerufen. Mache die ganze Liste platt.");
		tasks.clear();
		return "redirect:/";
	}

		// ==========================================
	// === VERSIONIERUNGS-BEISPIEL (AUFGABE) ====
	// ==========================================

	// Endpunkt Version 1: Nutzt die bestehende Klasse Task.java
	@CrossOrigin
	@GetMapping("/api/v1/task-example")
	public Task getTaskV1() {
		Task taskV1 = new Task();
		taskV1.setTaskdescription("Das ist ein Task aus der alten V1-Schnittstelle!");
		return taskV1;
	}

	// Endpunkt Version 2: Nutzt die neue Klasse TaskV2.java (inklusive Prioritaet)
	@CrossOrigin
	@GetMapping("/api/v2/task-example")
	public TaskV2 getTaskV2() {
		// Gibt eine V2 zurück mit der Priorität "1" (Höchste)
		return new TaskV2("Das ist ein Task aus der neuen V2-Schnittstelle!", 1);
	}

}
