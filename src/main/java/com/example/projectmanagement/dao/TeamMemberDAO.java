package com.example.projectmanagement.dao;

import com.example.projectmanagement.model.TeamMember;
import org.springframework.stereotype.Repository;
import com.example.projectmanagement.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamMemberDAO {

    public boolean createTeamMember(TeamMember member) {
        String sql = "INSERT INTO team_members (name, role) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getRole());
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Duplicate or invalid entry: " + member.getName());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TeamMember> getAllTeamMembers() {
        List<TeamMember> list = new ArrayList<>();
        String sql = "SELECT * FROM team_members";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TeamMember m = new TeamMember();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setRole(rs.getString("role"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateTeamMember(TeamMember member) {
        String sql = "UPDATE team_members SET name=?, role=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getRole());
            stmt.setInt(3, member.getId());
            int rows = stmt.executeUpdate();
            return rows > 0; // return false if no such ID

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTeamMember(int id) {
        String sql = "DELETE FROM team_members WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0; // return false if no such ID

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
