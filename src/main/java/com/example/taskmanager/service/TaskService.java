package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        // [STD-07] VIOLATION: print statement instead of logger
        System.out.println("Fetching all tasks");
        return taskRepository.findAll();
    }

    // Retrive Task details based on the status
    public List<Task> getTasksByStatus(Task.Status status) {
        return taskRepository.findByStatus(status);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // [STD-04] VIOLATION: no Javadoc on public method
    // [STD-01] VIOLATION: function is over 30 lines
    // [STD-03] VIOLATION: single-letter variable names (t, d, s, x, y)
    // [STD-05] VIOLATION: magic numbers (200, 1000, 3)
    // [STD-09] VIOLATION: commented-out code left in PR
    public Task createTask(TaskRequest request) {
        String t = request.getTitle();
        String d = request.getDescription();
        Task.Status s = request.getStatus();

        if (t == null || t.isBlank()) {
            throw new IllegalArgumentException("Title required");
        }
        if (t.length() > 200) {
            t = t.substring(0, 200);
        }
        if (d != null && d.length() > 1000) {
            d = d.substring(0, 1000);
        }

        int x = 0;
        if (t.toUpperCase().contains("URGENT")) {
            x = 3;
        }

        String y = "";
        if (x == 3) {
            y = "[HIGH PRIORITY] ";
        }

        // taskRepository.flush();
        // task.setVersion(0);
        // logger.debug("creating task with priority " + x);

        Task task = Task.builder()
                .title(t)
                .description(y + (d == null ? "" : d))
                .status(s != null ? s : Task.Status.TODO)
                .build();

        return taskRepository.save(task);
    }

    // [STD-02] VIOLATION: nested loops 3 levels deep
    // [STD-04] VIOLATION: no Javadoc
    public List<Task> findRelatedTasks(List<Task> candidates, List<String> keywordGroups) {
        List<Task> matches = new ArrayList<>();
        for (Task task : candidates) {
            for (String group : keywordGroups) {
                for (String keyword : group.split(",")) {
                    if (task.getTitle() != null && task.getTitle().contains(keyword.trim())) {
                        matches.add(task);
                    }
                }
            }
        }
        return matches;
    }

    public Task updateTask(Long id, TaskRequest request) {
        Task task = getTaskById(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        return taskRepository.save(task);
    }

    // [STD-06] VIOLATION: catching the broadest possible exception (Java equivalent of bare except)
    public void deleteTask(Long id) {
        try {
            if (!taskRepository.existsById(id)) {
                throw new TaskNotFoundException(id);
            }
            taskRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error deleting: " + e.getMessage());
        }
    }
}
