package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        task.setId(idCounter++);
        if (task.getCreationDate() == null) {
            task.setCreationDate(java.time.LocalDateTime.now().toString());
        }
        if (task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }
        tasks.add(task);
        return task;
    }

    public Task updateTask(Long id, Task updatedTask) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                if (updatedTask.getTaskdescription() != null) {
                    task.setTaskdescription(updatedTask.getTaskdescription());
                }
                if (updatedTask.getDueDate() != null) {
                    task.setDueDate(updatedTask.getDueDate());
                }
                if (updatedTask.getPriority() != null) {
                    task.setPriority(updatedTask.getPriority());
                }
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
    }
}
