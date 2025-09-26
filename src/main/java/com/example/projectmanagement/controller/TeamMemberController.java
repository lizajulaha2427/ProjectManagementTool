package com.example.projectmanagement.controller;

import com.example.projectmanagement.dao.TeamMemberDAO;
import com.example.projectmanagement.model.TeamMember;
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
    public String create(@RequestBody TeamMember member) {
        boolean success = memberDAO.createTeamMember(member);
        if (success) {
            return "Team member added!";
        } else {
            return "Error in adding team member (maybe duplicate or invalid input)";
        }
    }

    @GetMapping
    public List<TeamMember> getAll() {
        return memberDAO.getAllTeamMembers();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody TeamMember member) {
        member.setId(id);
        boolean success = memberDAO.updateTeamMember(member);
        if (success) {
            return "Team member updated!";
        } else {
            return "No team member found with ID: " + id;
        }
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
