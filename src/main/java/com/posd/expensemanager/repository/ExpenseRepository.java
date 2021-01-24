package com.posd.expensemanager.repository;

import com.posd.expensemanager.model.Expense;
import com.posd.expensemanager.model.TravelOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {

    Optional<Expense> findByTravelOrder(TravelOrder travelOrder);

}
