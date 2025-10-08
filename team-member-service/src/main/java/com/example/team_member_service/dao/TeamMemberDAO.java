package com.example.team_member_service.dao;

import com.example.team_member_service.model.TeamMember;
import org.springframework.stereotype.Repository;
import com.example.team_member_service.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamMemberDAO {

    public boolean createTeamMember(TeamMember member) {
    String checkSql = "SELECT COUNT(*) FROM team_members WHERE name = ?";
    String insertSql = "INSERT INTO team_members (name, role) VALUES (?, ?)";

    try (Connection conn = Database.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        // Check duplicate first
        checkStmt.setString(1, member.getName());
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Duplicate name exists
            }
        }

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, member.getName());
            insertStmt.setString(2, member.getRole());
            insertStmt.executeUpdate();
            return true;
        }

    } catch (SQLIntegrityConstraintViolationException ex) {
        System.out.println("Duplicate entry: " + member.getName());
        return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public boolean updateTeamMember(TeamMember member) {
    String checkSql = "SELECT COUNT(*) FROM team_members WHERE name = ? AND id <> ?";
    String updateSql = "UPDATE team_members SET name = ?, role = ? WHERE id = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        // Check for duplicates
        checkStmt.setString(1, member.getName());
        checkStmt.setInt(2, member.getId());
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Duplicate exists
            }
        }

        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setString(1, member.getName());
            updateStmt.setString(2, member.getRole());
            updateStmt.setInt(3, member.getId());
            int rows = updateStmt.executeUpdate();
            return rows > 0;
        }

    } catch (SQLIntegrityConstraintViolationException ex) {
        System.out.println("Duplicate entry on update: " + member.getName());
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
