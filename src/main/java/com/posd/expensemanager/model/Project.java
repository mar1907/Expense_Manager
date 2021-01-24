package com.posd.expensemanager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer budget;
    private Double totalPolicy;
    private Double expensePolicy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User supervisor;

    @ManyToOne
    private User clerk;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> users;

    public Project() {
    }

    public Project(String name, Integer budget, Double totalPolicy, Double expensePolicy, User supervisor, User clerk) {
        this.name = name;
        this.budget = budget;
        this.totalPolicy = totalPolicy;
        this.expensePolicy = expensePolicy;
        this.supervisor = supervisor;
        this.clerk = clerk;
        this.users = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public Double getTotalPolicy() {
        return totalPolicy;
    }

    public void setTotalPolicy(Double totalPolicy) {
        this.totalPolicy = totalPolicy;
    }

    public Double getExpensePolicy() {
        return expensePolicy;
    }

    public void setExpensePolicy(Double expensePolicy) {
        this.expensePolicy = expensePolicy;
    }

    public User getClerk() {
        return clerk;
    }

    public void setClerk(User clerk) {
        this.clerk = clerk;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String toString() {
        return name;
    }

    public String toCompleteString() {
        return id + " | " + name + " | " + budget + " | " + supervisor.getUsername() + " | " + clerk.getUsername();
    }
}
