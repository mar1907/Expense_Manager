package com.posd.expensemanager.service.travelorder;

import com.posd.expensemanager.model.Expense;
import com.posd.expensemanager.model.Project;
import com.posd.expensemanager.model.TravelOrder;
import com.posd.expensemanager.model.User;
import com.posd.expensemanager.repository.ExpenseRepository;
import com.posd.expensemanager.repository.ProjectRepository;
import com.posd.expensemanager.repository.TravelOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TravelOrderServiceImplementation implements TravelOrderService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TravelOrderRepository travelOrderRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public String addTravelOrder(Integer budget, String destination, String projectName, User me) {
        Project project = projectRepository.findByName(projectName).get(0);

        String msg = "Please approve travel order for project " + projectName + " destination: " + destination +
                " with budget: " + budget + " requested by: " + me.getUsername();

        TravelOrder travelOrder = new TravelOrder(budget, destination, me, project, "clerk", msg);
        travelOrderRepository.save(travelOrder);

        return "Travel Order created";
    }

    @Override
    public List<String> getFormattedTravelOrdersForClerk(User clerk) {
        List<Project> projectList = projectRepository.findByClerk(clerk);
        List<TravelOrder> travelOrders = new ArrayList<>();

        for (Project p: projectList) {
            List<TravelOrder> list = travelOrderRepository.findByProjectAndStatus(p, "clerk");
            travelOrders.addAll(list);
        }

        List<String> results = new ArrayList<>();

        for (TravelOrder t: travelOrders) {
            String s = t.toString();
            s += " | " + t.getProject().getTotalPolicy() * 100  + "%";
            s += " | " + t.getProject().getExpensePolicy() * 100 + "%";
            s += " | " + (isCompliant(t) ? "yes" : "no");

            results.add(s);
        }

        return results;
    }

    @Override
    public String approveClerkTravelOrder(Integer travelOrderId, User user) {
        Optional<TravelOrder> optional = travelOrderRepository.findById(travelOrderId);
        if (optional.isEmpty()) {
            return "Invalid travel order id";
        }

        TravelOrder travelOrder = optional.get();
        if(!isCompliant(travelOrder)) {
            return "Travel order is not compliant, cannot approve!";
        }

        travelOrder.setStatus("supervisor");
        String newMsg = travelOrder.getMessage() + " approved by clerk " + user.getUsername();
        travelOrder.setMessage(newMsg);

        travelOrderRepository.save(travelOrder);

        return "Approved";
    }

    @Override
    public String rejectClerkTravelOrder(Integer travelOrderId, String reason, User user) {
        Optional<TravelOrder> optional = travelOrderRepository.findById(travelOrderId);
        if (optional.isEmpty()) {
            return "Invalid travel order id";
        }

        TravelOrder travelOrder = optional.get();

        travelOrder.setStatus("rejected");
        String newMsg = "Travel order with id " + travelOrderId + " was rejected by " + user.getUsername() + " reason: " + reason;
        travelOrder.setMessage(newMsg);

        travelOrderRepository.save(travelOrder);

        return "Rejected";
    }

    @Override
    public List<String> getFormattedTravelOrdersForSupervisor(User supervisor) {
        List<Project> projectList = projectRepository.findBySupervisor(supervisor);
        List<TravelOrder> travelOrders = new ArrayList<>();

        for (Project p: projectList) {
            List<TravelOrder> list = travelOrderRepository.findByProjectAndStatus(p, "supervisor");
            travelOrders.addAll(list);
        }

        List<String> results = new ArrayList<>();

        for (TravelOrder t: travelOrders) {
            results.add(t.toString());
        }

        return results;
    }

    @Override
    public String approveSupervisorTravelOrder(Integer travelOrderId, User user) {
        Optional<TravelOrder> optional = travelOrderRepository.findById(travelOrderId);
        if (optional.isEmpty()) {
            return "Invalid travel order id";
        }

        TravelOrder travelOrder = optional.get();

        travelOrder.setStatus("approved");
        String newMsg = travelOrder.getMessage() + " approved by supervisor " + user.getUsername();
        travelOrder.setMessage(newMsg);

        travelOrderRepository.save(travelOrder);

        return "Approved";
    }

    @Override
    public String rejectSupervisorTravelOrder(Integer travelOrderId, String reason, User user) {
        Optional<TravelOrder> optional = travelOrderRepository.findById(travelOrderId);
        if (optional.isEmpty()) {
            return "Invalid travel order id";
        }

        TravelOrder travelOrder = optional.get();

        travelOrder.setStatus("rejected");
        String newMsg = "Travel order with id " + travelOrderId + " was rejected by " + user.getUsername() + " reason: " + reason;
        travelOrder.setMessage(newMsg);

        travelOrderRepository.save(travelOrder);

        return "Rejected";
    }

    @Override
    public List<TravelOrder> findByRequesterAndStatusNoExpense(User user, String status) {
        List<TravelOrder> travelOrderList = travelOrderRepository.findByRequesterAndStatus(user, status);
        List<TravelOrder> finalList = new ArrayList<>();

        for(TravelOrder t: travelOrderList) {
            Optional<Expense> expenseOptional = expenseRepository.findByTravelOrder(t);

            if(expenseOptional.isEmpty()) {
                finalList.add(t);
            }
        }

        return finalList;
    }

    private Boolean isCompliant(TravelOrder travelOrder) {
        Project project = travelOrder.getProject();
        // get a list of all approved travel orders for the project of this travel order
        List<TravelOrder> travelOrders =
                travelOrderRepository.findByProjectAndStatus(project, "approved");

        Double travelBudget = project.getBudget() * project.getTotalPolicy();
        Double travelBudgetPerTravel = travelBudget * project.getExpensePolicy();
        Double spent = 0.;

        // compute the amount spent for travel on this
        for (TravelOrder t: travelOrders) {
            spent += t.getBudget();
        }

        // if total spent plus the budget of this travel order greater than the travel budget
        if (spent + travelOrder.getBudget() > travelBudget) {
            return false;
        }

        // if the budget of this travel order is greater than the allowed budget for one travel order
        if (travelOrder.getBudget() > travelBudgetPerTravel) {
            return false;
        }

        return true;
    }
}
