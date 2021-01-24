package com.posd.expensemanager.service.expense;

import com.posd.expensemanager.model.Expense;
import com.posd.expensemanager.model.TravelOrder;
import com.posd.expensemanager.model.User;

import java.util.List;

public interface ExpenseService {

    String addExpense(Integer travelOrderId, String docs, User user);

    List<Expense> findAllForUser(User user);

    List<String> getFormattedExpenses(User user);

    String approveExpense(Integer expenseId, User user);

    String rejectExpense(Integer expenseId, String reason, User user);
}
