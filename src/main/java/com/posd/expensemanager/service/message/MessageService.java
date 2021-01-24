package com.posd.expensemanager.service.message;

import com.posd.expensemanager.model.User;

import java.util.List;

public interface MessageService {

    List<String> getMessagesForUser(User user);

    void markAsRead(String messageId);

    List<String> getMessagesIds(User user);

}
