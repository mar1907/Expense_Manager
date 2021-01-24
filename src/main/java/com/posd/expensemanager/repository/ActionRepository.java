package com.posd.expensemanager.repository;

import com.posd.expensemanager.model.Action;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Integer> {
}
