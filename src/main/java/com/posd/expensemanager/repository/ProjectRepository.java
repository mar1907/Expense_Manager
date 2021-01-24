package com.posd.expensemanager.repository;

import com.posd.expensemanager.model.Project;
import com.posd.expensemanager.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Integer> {

    List<Project> findByName(String projectName);
    List<Project> findBySupervisor(User user);
    List<Project> findByClerk(User user);
    List<Project> findAll();

}
