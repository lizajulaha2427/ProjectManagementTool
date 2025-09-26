package com.example.projectmanagement;

import com.example.projectmanagement.dao.ProjectDAO;
import com.example.projectmanagement.dao.TaskDAO;
import com.example.projectmanagement.dao.TeamMemberDAO;
import com.example.projectmanagement.model.Project;
import com.example.projectmanagement.model.Task;
import com.example.projectmanagement.model.TeamMember;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
public class AppMenu implements CommandLineRunner {

    private final ProjectDAO projectDAO;
    private final TaskDAO taskDAO;
    private final TeamMemberDAO teamMemberDAO;

    public AppMenu(ProjectDAO projectDAO, TaskDAO taskDAO, TeamMemberDAO teamMemberDAO) {
        this.projectDAO = projectDAO;
        this.taskDAO = taskDAO;
        this.teamMemberDAO = teamMemberDAO;
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n=== Project Management Menu ===");
                System.out.println("1. Manage Projects");
                System.out.println("2. Manage Tasks");
                System.out.println("3. Manage Team Members");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> manageProjects(sc);
                    case 2 -> manageTasks(sc);
                    case 3 -> manageTeamMembers(sc);
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine();
            }
        }
    }

    private void manageProjects(Scanner sc) {
        System.out.println("\n--- Project Menu ---");
        System.out.println("1. Create Project");
        System.out.println("2. Get All Projects");
        System.out.println("3. Update Project");
        System.out.println("4. Delete Project");
        System.out.print("Choose: ");
        int ch = sc.nextInt(); sc.nextLine();

        switch (ch) {
            case 1 -> {
                try {
                    System.out.print("Enter name: "); String name = sc.nextLine();
                    System.out.print("Enter description: "); String desc = sc.nextLine();
                    System.out.print("Enter deadline (YYYY-MM-DD): "); String dl = sc.nextLine();
                    Project p = new Project(0, name, desc, LocalDate.parse(dl));
                    boolean success = projectDAO.createProject(p);
                    if (success) {
                        System.out.println("Project created!");
                    } else {
                        System.out.println("Project with name '" + name + "' already exists!");
                    }
                } catch (Exception e) {
                    System.out.println("Failed to create project: " + e.getMessage());
                }
            }
            case 2 -> {
                List<Project> projects = projectDAO.getAllProjects();
                if (projects.isEmpty()) {
                    System.out.println("No projects found.");
                } else {
                    projects.forEach(System.out::println);
                }
            }
            case 3 -> {
                try {
                    System.out.print("Enter project ID to update: "); int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter new name: "); String name = sc.nextLine();
                    System.out.print("Enter new description: "); String desc = sc.nextLine();
                    System.out.print("Enter new deadline (YYYY-MM-DD): "); String dl = sc.nextLine();
                    Project p = new Project(id, name, desc, LocalDate.parse(dl));
                    boolean updated = projectDAO.updateProject(p);
                    if (updated) {
                        System.out.println("Project updated!");
                    } else {
                        System.out.println("No project found with ID: " + id);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to update project: " + e.getMessage());
                }
            }
            case 4 -> {
                try {
                    System.out.print("Enter project ID to delete: "); int id = sc.nextInt();
                    boolean deleted = projectDAO.deleteProject(id);
                    if (deleted) {
                        System.out.println("Project deleted!");
                    } else {
                        System.out.println("No project found with ID: " + id);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to delete project: " + e.getMessage());
                }
            }
            default -> System.out.println("Invalid choice");
        }
    }

    private void manageTasks(Scanner sc) {
        System.out.println("\n--- Task Menu ---");
        System.out.println("1. Create Task");
        System.out.println("2. Get All Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Delete Task");
        System.out.print("Choose: ");
        int ch = sc.nextInt(); sc.nextLine();

        switch (ch) {
            case 1 -> {
                System.out.print("Enter project ID: "); 
                int pid = sc.nextInt(); sc.nextLine();
                System.out.print("Enter Name: "); 
                String title = sc.nextLine();
                System.out.print("Enter status: "); 
                String status = sc.nextLine();

                Task t = new Task(0, pid, title, status);
                boolean success = taskDAO.createTask(t);

                if (success) {
                    System.out.println("Task created!");
                } else {
                    System.out.println("Failed to create task (duplicate or invalid input)");
                }
            }

            case 2 -> {
                List<Task> tasks = taskDAO.getAllTasks();
                if (tasks.isEmpty()) {
                    System.out.println("No tasks found.");
                } else {
                    tasks.forEach(System.out::println);
                }
            }
            case 3 -> {
                try {
                    System.out.print("Enter task ID to update: "); int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter new project ID: "); int pid = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter new title: "); String title = sc.nextLine();
                    System.out.print("Enter new status: "); String status = sc.nextLine();
                    Task t = new Task(id, pid, title, status);
                    boolean updated = taskDAO.updateTask(t);
                    if (updated) {
                        System.out.println("Task updated!");
                    } else {
                        System.out.println("No task found with ID: " + id);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to update task: " + e.getMessage());
                }
            }
            case 4 -> {
                try {
                    System.out.print("Enter task ID to delete: "); int id = sc.nextInt();
                    boolean deleted = taskDAO.deleteTask(id);
                    if (deleted) {
                        System.out.println("Task deleted!");
                    } else {
                        System.out.println("No task found with ID: " + id);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to delete task: " + e.getMessage());
                }
            }
        }
    }

    private void manageTeamMembers(Scanner sc) {
        System.out.println("\n--- Team Member Menu ---");
        System.out.println("1. Create Team Member");
        System.out.println("2. Get All Team Members");
        System.out.println("3. Update Team Member");
        System.out.println("4. Delete Team Member");
        System.out.print("Choose: ");
        int ch = sc.nextInt(); sc.nextLine();

        switch (ch) {
            case 1 -> {
                try {
                    System.out.print("Enter name: "); String name = sc.nextLine();
                    System.out.print("Enter role: "); String role = sc.nextLine();
                    TeamMember tm = new TeamMember(0, name, role);
                    boolean success = teamMemberDAO.createTeamMember(tm);
                    if (success) {
                        System.out.println("Team member created!");
                    } else {
                        System.out.println("Team member with name '" + name + "' already exists!");
                    }
                } catch (Exception e) {
                    System.out.println("Failed to create team member: " + e.getMessage());
                }
            }
            case 2 -> {
                List<TeamMember> members = teamMemberDAO.getAllTeamMembers();
                if (members.isEmpty()) {
                    System.out.println("No team members found.");
                } else {
                    members.forEach(System.out::println);
                }
            }
            case 3 -> {
                try {
                    System.out.print("Enter team member ID to update: "); int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter new name: "); String name = sc.nextLine();
                    System.out.print("Enter new role: "); String role = sc.nextLine();
                    TeamMember tm = new TeamMember(id, name, role);
                    boolean updated = teamMemberDAO.updateTeamMember(tm);
                    if (updated) {
                        System.out.println("Team member updated!");
                    } else {
                        System.out.println("No team member found with ID: " + id);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to update team member: " + e.getMessage());
                }
            }
            case 4 -> {
                try {
                    System.out.print("Enter team member ID to delete: "); int id = sc.nextInt();
                    boolean deleted = teamMemberDAO.deleteTeamMember(id);
                    if (deleted) {
                        System.out.println("Team member deleted!");
                    } else {
                        System.out.println("No team member found with ID: " + id);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to delete team member: " + e.getMessage());
                }
            }
        }
    }
}
