package com.posd.expensemanager.repository;

import com.posd.expensemanager.model.Role;
import com.posd.expensemanager.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    List<User> findByRole(Role role);

}
