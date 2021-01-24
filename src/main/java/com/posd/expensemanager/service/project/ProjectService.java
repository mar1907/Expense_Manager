package com.posd.expensemanager.service.project;

import com.posd.expensemanager.model.Project;
import com.posd.expensemanager.model.User;

import java.util.List;

public interface ProjectService {

    Boolean addProject(String name, Integer budget, Double budgetFraction, Double budgetFractionTravel, String clerkName, User me);

    String addUserToProject(String userName, String projectName);

    List<Project> findProjectsForUser(User user);

    List<String> getProjectReports(User user);

}
