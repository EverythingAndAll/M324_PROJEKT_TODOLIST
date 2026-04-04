package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@SpringBootApplication
@CrossOrigin(origins = "*")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private final TaskService taskService;

	// Injektion des neuen TaskServices
	public DemoApplication(TaskService taskService) {
		this.taskService = taskService;
	}

	// ==========================================
	// === NEUE REST-ENDPUNKTE (REFACTORING) ====
	// ==========================================

	@GetMapping("/")
	public List<Task> getRoot() {
		// Fallback fuer den alten Frontend-Code, der auf "/" hoert
		return taskService.getAllTasks(null, null, null);
	}

	@GetMapping("/tasks")
	public ResponseEntity<List<Task>> getTasks(
			@RequestParam(required = false) String priority,
			@RequestParam(required = false) Boolean completed,
			@RequestParam(required = false) String search) {
		
		List<Task> filteredTasks = taskService.getAllTasks(priority, completed, search);
		return ResponseEntity.ok(filteredTasks);
	}

	@PostMapping("/tasks")
	public ResponseEntity<?> addTask(@RequestBody Task task) {
		try {
			Task createdTask = taskService.createTask(task);
			return ResponseEntity.ok(createdTask);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
		}
	}

	@PutMapping("/tasks/{id}")
	public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
		try {
			Task task = taskService.updateTask(id, updatedTask);
			if (task != null) {
				return ResponseEntity.ok(task);
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		boolean deleted = taskService.deleteTask(id);
		if (deleted) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	// Fallback für alte Frontend-Route, bis es komplett umgestellt ist
	@PostMapping("/delete")
	public String delTaskLegacy(@RequestBody Task task) {
		// Diese Methode ist eigentlich veraltet, REST-konform ist DELETE /tasks/{id}
		return "redirect:/";
	}

	@PostMapping("/clear")
	public String clearTasks() {
		taskService.clearAll();
		return "redirect:/";
	}

	// ==========================================
	// === VERSIONIERUNGS-BEISPIEL (AUFGABE) ====
	// ==========================================

	@GetMapping("/api/v1/task-example")
	public Task getTaskV1() {
		Task task = new Task();
		task.setTaskdescription("V1 Demo");
		return task;
	}

	@GetMapping("/api/v2/task-example")
	public TaskV2 getTaskV2() {
		return new TaskV2("V2 Demo", 1);
	}

	private Map<String, String> errorResponse(String message) {
		Map<String, String> error = new LinkedHashMap<>();
		error.put("message", message);
		return error;
	}
}
