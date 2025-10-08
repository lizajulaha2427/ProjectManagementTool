package com.example.task_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private int id;
    
    @JsonProperty("project_id")
    private int projectId;
    private String name;
    private String status; 
  
    public Task() {}

    public Task(int id, int projectId, String name, String status) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Task {" +
                "id=" + id +
                ", projectId=" + projectId +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
