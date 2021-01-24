package com.posd.expensemanager.service.project;

import com.posd.expensemanager.model.Project;
import com.posd.expensemanager.model.TravelOrder;
import com.posd.expensemanager.model.User;
import com.posd.expensemanager.repository.ProjectRepository;
import com.posd.expensemanager.repository.TravelOrderRepository;
import com.posd.expensemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImplementation implements ProjectService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TravelOrderRepository travelOrderRepository;

    @Override
    public Boolean addProject(String name, Integer budget, Double budgetFraction, Double budgetFractionTravel, String clerkName, User me) {
        List<User> clerkList = userRepository.findByUsername(clerkName);
        if(clerkList.isEmpty()){
            return false;
        }
        User clerk = clerkList.get(0);
        Project project = new Project(name, budget, budgetFraction, budgetFractionTravel, me, clerk);
        projectRepository.save(project);

        return true;
    }

    @Override
    public String addUserToProject(String userName, String projectName) {
        Project project = projectRepository.findByName(projectName).get(0);
        User user = userRepository.findByUsername(userName).get(0);

        if (project.getUsers().contains(user))
            return "Already exists";

        project.getUsers().add(user);
        projectRepository.save(project);

        return "Added";
    }

    @Override
    public List<Project> findProjectsForUser(User user) {
        List<Project> projectList = new ArrayList<>();

        List<Project> bySupervisor = projectRepository.findBySupervisor(user);
        List<Project> byClerk = projectRepository.findByClerk(user);

        projectList.addAll(bySupervisor);
        projectList.addAll(byClerk);

        List<Project> allProjects = projectRepository.findAll();
        for(Project p: allProjects) {
            if(p.getUsers().contains(user)) {
                projectList.add(p);
            }
        }

        return projectList;
    }

    @Override
    public List<String> getProjectReports(User user) {
        List<String> result = new ArrayList<>();
        if(!user.getRole().getName().equals("supervisor")) {
            return result;
        }
        String tab = "----";

        result.add("ID | Name | Budget | Supervisor | Clerk");

        List<Project> projectList = projectRepository.findBySupervisor(user);
        for(Project p: projectList) {
            result.add(p.toCompleteString());
            result.add(tab + "Travel Orders:");
            result.add(tab + tab + "ID | Budget | Destination | Status | Project");
            List<TravelOrder> travelOrderList = travelOrderRepository.findByProject(p);

            Integer spent = 0;
            for(TravelOrder t: travelOrderList) {
                result.add(tab + tab + t.toString());
                if(t.getStatus().equals("approved")) {
                    spent += t.getBudget();
                }
            }

            result.add(tab + "Spent: " + spent);
            result.add(tab + "Remaining: " + (p.getBudget() - spent));
        }

        return result;
    }
}
