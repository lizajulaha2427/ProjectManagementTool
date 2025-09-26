package com.example.projectmanagement.controller;

import com.example.projectmanagement.dao.TaskDAO;
import com.example.projectmanagement.model.Task;
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
    public String create(@RequestBody Task task) {
        boolean success = taskDAO.createTask(task);
        if (success) {
            return "Task created!";
        } else {
            return "Failed to create task (duplicate name for project or DB error)";
        }
    }

    @GetMapping
    public List<Task> getAll() {
        return taskDAO.getAllTasks();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody Task task) {
        task.setId(id);
        boolean success = taskDAO.updateTask(task);
        if (success) {
            return "Task updated!";
        } else {
            return "No task found with ID: " + id;
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
