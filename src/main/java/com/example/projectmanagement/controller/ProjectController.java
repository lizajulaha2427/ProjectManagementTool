package com.example.projectmanagement.controller;

import com.example.projectmanagement.dao.ProjectDAO;
import com.example.projectmanagement.model.Project;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectDAO projectDAO;

    public ProjectController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @PostMapping
    public String create(@RequestBody Project project) {
        boolean success = projectDAO.createProject(project);
        if (success) {
            return "Project created!";
        } else {
            return "Project with name '" + project.getName() + "' already exists or failed!";
        }
    }

    @GetMapping
    public List<Project> getAll() {
        return projectDAO.getAllProjects();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody Project project) {
        project.setId(id);
        boolean success = projectDAO.updateProject(project);
        if (success) {
            return "Project updated!";
        } else {
            return "No project found with ID: " + id;
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean success = projectDAO.deleteProject(id);
        if (success) {
            return "Project deleted!";
        } else {
            return "No project found with ID: " + id;
        }
    }
}
