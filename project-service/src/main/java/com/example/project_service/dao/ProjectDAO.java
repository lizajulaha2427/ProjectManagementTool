package com.example.project_service.dao;

import com.example.project_service.config.Database;
import com.example.project_service.model.Project;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDAO {

    public boolean createProject(Project project) {
        String sql = "INSERT INTO projects (name, description, deadline) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setDate(3, project.getDeadline() != null ? java.sql.Date.valueOf(project.getDeadline()) : null);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Duplicate project name: " + project.getName());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Project> getAllProjects() {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Project p = new Project();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));

                Date sqlDate = rs.getDate("deadline");
                if (sqlDate != null) {
                    p.setDeadline(sqlDate.toLocalDate());
                }
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int updateProject(Project project) {
    String checkSql = "SELECT COUNT(*) FROM projects WHERE name = ? AND id <> ?";
    String updateSql = "UPDATE projects SET name=?, description=?, deadline=? WHERE id=?";

    try (Connection conn = Database.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql);
         PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

        checkStmt.setString(1, project.getName());
        checkStmt.setInt(2, project.getId());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            return -1;
        }

        updateStmt.setString(1, project.getName());
        updateStmt.setString(2, project.getDescription());
        updateStmt.setDate(3, project.getDeadline() != null ? java.sql.Date.valueOf(project.getDeadline()) : null);
        updateStmt.setInt(4, project.getId());

        int rows = updateStmt.executeUpdate();
        if (rows > 0) {
            return 1; 
        } else {
            return 0; 
        }

    } catch (Exception e) {
        e.printStackTrace();
        return 0;
    }
}


    public boolean deleteProject(int id) {
        String sql = "DELETE FROM projects WHERE id=?";
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
