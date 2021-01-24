package com.posd.expensemanager.repository;

import com.posd.expensemanager.model.Project;
import com.posd.expensemanager.model.TravelOrder;
import com.posd.expensemanager.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TravelOrderRepository extends CrudRepository<TravelOrder, Integer> {

    List<TravelOrder> findByRequester(User user);

    List<TravelOrder> findByProjectAndStatus(Project project, String status);

    List<TravelOrder> findByRequesterAndStatus(User user, String status);

    List<TravelOrder> findByProject(Project project);

}
