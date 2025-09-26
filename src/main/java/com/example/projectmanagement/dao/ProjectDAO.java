package com.example.projectmanagement.dao;

import com.example.projectmanagement.config.Database;
import com.example.projectmanagement.model.Project;
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
            stmt.setDate(3, java.sql.Date.valueOf(project.getDeadline()));

            int rows = stmt.executeUpdate();
            return rows > 0; // true if inserted

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

    public boolean updateProject(Project project) {
        String sql = "UPDATE projects SET name=?, description=?, deadline=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(project.getDeadline()));
            stmt.setInt(4, project.getId());

            int rows = stmt.executeUpdate();
            return rows > 0; // return false if no such ID exists

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProject(int id) {
        String sql = "DELETE FROM projects WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0; // return false if no such ID exists

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
