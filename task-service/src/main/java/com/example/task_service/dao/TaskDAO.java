package com.example.task_service.dao;

import com.example.task_service.model.Task;
import org.springframework.stereotype.Repository;
import com.example.task_service.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskDAO {

    public boolean createTask(Task task) {
        String checkSql = "SELECT COUNT(*) FROM tasks WHERE project_id=? AND name=?";
        String insertSql = "INSERT INTO tasks (project_id, name, status) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, task.getProjectId());
            checkStmt.setString(2, task.getName());

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, task.getProjectId());
                insertStmt.setString(2, task.getName());
                insertStmt.setString(3, task.getStatus());
                int rows = insertStmt.executeUpdate();
                return rows > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setProjectId(rs.getInt("project_id"));
                t.setName(rs.getString("name"));
                t.setStatus(rs.getString("status"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateTask(Task task) {
    String sql = "UPDATE tasks SET project_id=?, name=?, status=? WHERE id=?";
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, task.getProjectId());
        stmt.setString(2, task.getName());
        stmt.setString(3, task.getStatus());
        stmt.setInt(4, task.getId());

        int rows = stmt.executeUpdate();
        return rows > 0;

    } catch (SQLIntegrityConstraintViolationException e) {
        System.err.println("Duplicate task name: " + task.getName());
        return false; 
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean checkProjectExists(int projectId) {
    String sql = "SELECT COUNT(*) FROM projects WHERE id=?";
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, projectId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}