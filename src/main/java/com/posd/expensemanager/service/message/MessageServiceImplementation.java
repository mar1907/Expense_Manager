package com.posd.expensemanager.service.message;

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
import java.util.stream.Collectors;

@Service
public class MessageServiceImplementation implements MessageService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TravelOrderRepository travelOrderRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public List<String> getMessagesForUser(User user) {
        List<String> messageList = new ArrayList<>();

        if (user.getRole().getName().equals("supervisor")) {
            messageList = getMessagesForRoleSupervisor(user);
            // add list of travel orders and expenses
        }
        else if (user.getRole().getName().equals("clerk")) {
            messageList = getMessagesForRoleClerk(user);
        }
        else if (user.getRole().getName().equals("user")) {
            messageList = getMessagesForRoleUser(user);
        }
        else {
            return messageList;
        }


        return messageList;
    }

    @Override
    public void markAsRead(String messageId) {
        String[] array = messageId.split(" ");
        Integer id = Integer.parseInt(array[0]);
        String type = array[2];

        if(type.equals("Expense")) {
            Optional<Expense> expenseOptional = expenseRepository.findById(id);
            if(expenseOptional.isPresent()) {
                Expense expense = expenseOptional.get();
                String msg = expense.getMessage();
                msg = "Read " + msg;
                expense.setMessage(msg);

                expenseRepository.save(expense);
            }
        }
        else if(type.equals("Travel")) {
            Optional<TravelOrder> travelOrderOptional = travelOrderRepository.findById(id);
            if(travelOrderOptional.isPresent()) {
                TravelOrder travelOrder = travelOrderOptional.get();
                String msg = travelOrder.getMessage();
                msg = "Read " + msg;
                travelOrder.setMessage(msg);

                travelOrderRepository.save(travelOrder);
            }
        }
    }

    @Override
    public List<String> getMessagesIds(User user) {
        List<String> messageList = getMessagesForRoleUser(user);

        return messageList
                .stream()
                .filter(s -> !s.split(" ")[0].equals("Read"))
                .map(s -> {
                    String[] array = s.split(" ");
                    return array[0] + " " + array[1] + " " + array[2];
                })
                .collect(Collectors.toList());
    }

    private List<String> getMessagesForRoleSupervisor(User user) {
        List<String> messageList = new ArrayList<>();

        List<Project> projectList = projectRepository.findBySupervisor(user);
        for(Project p: projectList) {
            List<TravelOrder> travelOrderList = travelOrderRepository.findByProjectAndStatus(p, "supervisor");
            for(TravelOrder t: travelOrderList) {
                String msg = t.getId() + " | Travel Order | message: " + t.getMessage();
                messageList.add(msg);
            }
        }

        messageList.addAll(getMessagesForRoleUser(user));

        return messageList;
    }

    private List<String> getMessagesForRoleClerk(User user) {
        List<String> messageList = new ArrayList<>();

        List<Project> projectList = projectRepository.findByClerk(user);
        for(Project p: projectList) {
            List<TravelOrder> travelOrderList = travelOrderRepository.findByProjectAndStatus(p, "clerk");
            for(TravelOrder t: travelOrderList) {
                String msg = t.getId() + " | Travel Order | message: " + t.getMessage();
                messageList.add(msg);
            }
        }

        for(Project p: projectList) {
            List<TravelOrder> travelOrders = travelOrderRepository.findByProjectAndStatus(p, "approved");

            for (TravelOrder t : travelOrders) {
                Optional<Expense> expenseOptional = expenseRepository.findByTravelOrder(t);
                if(expenseOptional.isPresent()) {
                    Expense expense = expenseOptional.get();
                    if(expense.getStatus().equals("clerk")) {
                        String msg = expense.getId() + " | Expense | message: " + expense.getMessage();
                        messageList.add(msg);
                    }
                }
            }
        }

        return messageList;
    }

    private List<String> getMessagesForRoleUser(User user) {
        List<String> messageList = new ArrayList<>();

        List<TravelOrder> travelOrderList =
                travelOrderRepository.findByRequester(user)
                        .stream()
                        .filter(t -> t.getStatus().equals("approved") || t.getStatus().equals("rejected"))
                        .collect(Collectors.toList());

        for (TravelOrder t: travelOrderList) {
            if (!t.getMessage().split(" ")[0].equals("Read")) {
                String msg = t.getId() + " | Travel Order | message: " + t.getMessage();
                messageList.add(msg);
            }
        }

        for (TravelOrder t: travelOrderList) {
            Optional<Expense> expenseOptional = expenseRepository.findByTravelOrder(t);
            if(expenseOptional.isPresent()) {
                Expense expense = expenseOptional.get();
                if(expense.getStatus().equals("approved") || expense.getStatus().equals("rejected")) {
                    if(!expense.getMessage().split(" ")[0].equals("Read")) {
                        String msg = expense.getId() + " | Expense | message: " + expense.getMessage();
                        messageList.add(msg);
                    }
                }
            }
        }

        return messageList;
    }
}
