package com.posd.expensemanager.model;

import javax.persistence.*;

@Entity
@Table(name = "travelorder")
public class TravelOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer budget;
    private String destination;
    private String status;
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public TravelOrder(Integer budget, String destination, User requester, Project project, String status, String message) {
        this.budget = budget;
        this.destination = destination;
        this.requester = requester;
        this.project = project;
        this.status = status;
        this.message = message;
    }

    public TravelOrder() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return id + " | " + budget + " | " + destination + " | " + status + " | " + project.getName();
    }
}
