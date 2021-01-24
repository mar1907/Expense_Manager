package com.posd.expensemanager.service.expense;

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
public class ExpenseServiceImplementation implements ExpenseService {

    @Autowired
    private TravelOrderRepository travelOrderRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public String addExpense(Integer travelOrderId, String docs, User user) {
        Optional<TravelOrder> travelOrderOptional = travelOrderRepository.findById(travelOrderId);

        if(travelOrderOptional.isEmpty()) {
            return "Travel order does not exist";
        }

        TravelOrder travelOrder = travelOrderOptional.get();

        if(!travelOrder.getStatus().equals("approved")) {
            return "Cannot create expense for non-approved travel orders";
        }

        Optional<Expense> expenseOptional = expenseRepository.findByTravelOrder(travelOrder);
        if(expenseOptional.isPresent()) {
            return "Expense already exists for this travel order";
        }

        String msg = "Please approve expense for travel order with id " + travelOrder.getId()
                + " with docs " + docs + " requested by " + user.getUsername();
        Expense expense = new Expense(docs, "clerk", msg, travelOrder);

        expenseRepository.save(expense);

        return "Created";
    }

    @Override
    public List<Expense> findAllForUser(User user) {
        List<TravelOrder> travelOrderList = travelOrderRepository.findByRequester(user);
        List<Expense> expenseList = new ArrayList<>();

        for(TravelOrder t: travelOrderList) {
            Optional<Expense> expenseOptional = expenseRepository.findByTravelOrder(t);
            if (expenseOptional.isPresent()) {
                expenseList.add(expenseOptional.get());
            }
        }

        return expenseList;
    }

    @Override
    public List<String> getFormattedExpenses(User user) {
        List<Project> projectList = projectRepository.findByClerk(user);
        List<String> result = new ArrayList<>();

        for(Project p: projectList) {
            List<TravelOrder> travelOrders = travelOrderRepository.findByProjectAndStatus(p, "approved");

            for(TravelOrder t: travelOrders) {
                Optional<Expense> expenseOptional = expenseRepository.findByTravelOrder(t);
                if(expenseOptional.isPresent()) {
                    Expense expense = expenseOptional.get();
                    if (!expense.getStatus().equals("approved")) {
                        result.add(expenseOptional.get().toFormattedString());
                    }
                }
            }
        }

        return result;
    }

    @Override
    public String approveExpense(Integer expenseId, User user) {
        Optional<Expense> optional = expenseRepository.findById(expenseId);
        if(optional.isEmpty()) {
            return "Invalid expense id";
        }

        Expense expense = optional.get();

        expense.setStatus("approved");
        String newMsg = expense.getMessage() + " approved by clerk " + user.getUsername();
        expense.setMessage(newMsg);

        expenseRepository.save(expense);

        return "Approved";
    }

    @Override
    public String rejectExpense(Integer expenseId, String reason, User user) {
        Optional<Expense> optional = expenseRepository.findById(expenseId);
        if(optional.isEmpty()) {
            return "Invalid expense id";
        }

        Expense expense = optional.get();

        expense.setStatus("rejected");
        String newMsg = expense.getMessage() + " was rejected by clerk " + user.getUsername() + " rejected: " + reason;
        expense.setMessage(newMsg);

        expenseRepository.save(expense);

        return "Rejected";
    }
}
