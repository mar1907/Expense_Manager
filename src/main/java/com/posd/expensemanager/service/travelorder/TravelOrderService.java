package com.posd.expensemanager.service.travelorder;

import com.posd.expensemanager.model.TravelOrder;
import com.posd.expensemanager.model.User;

import java.util.List;

public interface TravelOrderService {

    String addTravelOrder(Integer budget, String destination, String projectName, User me);

    List<String> getFormattedTravelOrdersForClerk(User clerk);

    String approveClerkTravelOrder(Integer travelOrderId, User user);

    String rejectClerkTravelOrder(Integer travelOrderId, String reason, User user);

    List<String> getFormattedTravelOrdersForSupervisor(User supervisor);

    String approveSupervisorTravelOrder(Integer travelOrderId, User user);

    String rejectSupervisorTravelOrder(Integer travelOrderId, String reason, User user);

    List<TravelOrder> findByRequesterAndStatusNoExpense(User user, String status);

}
