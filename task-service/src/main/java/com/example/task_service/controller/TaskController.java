package com.example.task_service.controller;

import com.example.task_service.dao.TaskDAO;
import com.example.task_service.model.Task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskDAO taskDAO;

    public TaskController(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

  @PostMapping
public ResponseEntity<String> create(@RequestBody Task task) {
    boolean projectExists = taskDAO.checkProjectExists(task.getProjectId());
    if (!projectExists) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Project ID does not exist!");
    }

    boolean success = taskDAO.createTask(task);
    if (success) {
        return ResponseEntity.ok("Task created!");
    } else {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Task with the same name for this project already exists!");
    }
}


    @GetMapping
    public List<Task> getAll() {
        return taskDAO.getAllTasks();
    }

    @PutMapping("/{id}")
public ResponseEntity<String> update(@PathVariable int id, @RequestBody Task task) {
    task.setId(id);

    boolean projectExists = taskDAO.checkProjectExists(task.getProjectId());
    if (!projectExists) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Project ID does not exist!");
    }

    boolean success = taskDAO.updateTask(task);
    if (success) {
        return ResponseEntity.ok("Task updated successfully!");
    } else {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Task with the same name already exists, or Task ID not found!");
    }
}

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean success = taskDAO.deleteTask(id);
        if (success) {
            return "Task deleted!";
        } else {
            return "No task found with ID: " + id;
        }
    }
}
