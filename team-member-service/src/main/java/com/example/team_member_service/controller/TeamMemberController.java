package com.example.team_member_service.controller;

import com.example.team_member_service.dao.TeamMemberDAO;
import com.example.team_member_service.model.TeamMember;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class TeamMemberController {

    private final TeamMemberDAO memberDAO;

    public TeamMemberController(TeamMemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }
   @PostMapping
public ResponseEntity<String> create(@RequestBody TeamMember member) {
    boolean success = memberDAO.createTeamMember(member);
    if (success) {
        return ResponseEntity.ok("Team member added!");
    } else {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Team member with name '" + member.getName() + "' already exists!");
    }
}

@PutMapping("/{id}")
public ResponseEntity<String> update(@PathVariable int id, @RequestBody TeamMember member) {
    member.setId(id);
    boolean success = memberDAO.updateTeamMember(member);
    if (success) {
        return ResponseEntity.ok("Team member updated!");
    } else {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Failed to update. Team member with name '" + member.getName() + "' may already exist or ID does not exist!");
    }
}

    @GetMapping
    public List<TeamMember> getAll() {
        return memberDAO.getAllTeamMembers();
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean success = memberDAO.deleteTeamMember(id);
        if (success) {
            return "Team member deleted!";
        } else {
            return "No team member found with ID: " + id;
        }
    }
}
