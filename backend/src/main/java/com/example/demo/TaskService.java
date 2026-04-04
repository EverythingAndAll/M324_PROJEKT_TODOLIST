package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private Long idCounter = 1L;

    public List<Task> getAllTasks(String priority, Boolean completed, String search) {
        return tasks.stream()
                .filter(task -> priority == null || priority.isEmpty() || priority.equals(task.getPriority()))
                .filter(task -> completed == null || completed.equals(task.isCompleted()))
                .filter(task -> search == null || search.isEmpty() || 
                                (task.getTaskdescription() != null && task.getTaskdescription().toLowerCase().contains(search.toLowerCase())))
                .collect(Collectors.toList());
    }

    public Task createTask(Task task) {
        validateTask(task, null);
        task.setId(idCounter++);
        if (task.getCreationDate() == null) {
            task.setCreationDate(java.time.LocalDateTime.now().toString());
        }
        task.setPriority(normalizePriority(task.getPriority()));
        tasks.add(task);
        return task;
    }

    public Task updateTask(Long id, Task updatedTask) {
        validateTask(updatedTask, id);
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                if (updatedTask.getTaskdescription() != null) {
                    task.setTaskdescription(updatedTask.getTaskdescription().trim());
                }
                if (updatedTask.getDueDate() != null) {
                    task.setDueDate(updatedTask.getDueDate());
                }
                task.setPriority(normalizePriority(updatedTask.getPriority()));
                task.setCompleted(updatedTask.isCompleted());
                return task;
            }
        }
        return null; // Not found
    }

    public boolean deleteTask(Long id) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public void clearAll() {
        tasks.clear();
        idCounter = 1L;
    }

    private void validateTask(Task task, Long currentTaskId) {
        if (task == null) {
            throw new IllegalArgumentException("Task payload darf nicht leer sein.");
        }

        String description = task.getTaskdescription();
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Aufgabenbeschreibung darf nicht leer sein.");
        }

        if (isDuplicateDescription(description, currentTaskId)) {
            throw new IllegalArgumentException("Eine Aufgabe mit derselben Beschreibung existiert bereits.");
        }

        String priority = normalizePriority(task.getPriority());
        if (!List.of("HIGH", "MEDIUM", "LOW").contains(priority)) {
            throw new IllegalArgumentException("Prioritaet muss HIGH, MEDIUM oder LOW sein.");
        }
    }

    private boolean isDuplicateDescription(String description, Long currentTaskId) {
        String normalizedDescription = description.trim();
        return tasks.stream()
                .filter(task -> currentTaskId == null || !task.getId().equals(currentTaskId))
                .anyMatch(task -> task.getTaskdescription() != null
                        && task.getTaskdescription().trim().equalsIgnoreCase(normalizedDescription));
    }

    private String normalizePriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            return "MEDIUM";
        }
        return priority.trim().toUpperCase(Locale.ROOT);
    }
}
