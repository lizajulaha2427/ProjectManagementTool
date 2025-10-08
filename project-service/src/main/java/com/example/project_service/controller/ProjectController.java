package com.example.project_service.controller;

import com.example.project_service.dao.ProjectDAO;
import com.example.project_service.model.Project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> create(@RequestBody Project project) {
        boolean success = projectDAO.createProject(project);
        if (success) {
            return ResponseEntity.ok("Project created!");
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Project with name '" + project.getName() + "' already exists!");
        }
    }

    @GetMapping
    public List<Project> getAll() {
        return projectDAO.getAllProjects();
    }

    @PutMapping("/{id}")
public ResponseEntity<String> update(@PathVariable int id, @RequestBody Project project) {
    project.setId(id);
    int result = projectDAO.updateProject(project);

    if (result == 1) {
        return ResponseEntity.ok("Project updated!");
    } else if (result == -1) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Project with the same name already exists!");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No project found with ID: " + id);
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

