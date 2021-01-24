package com.posd.expensemanager.repository;

import com.posd.expensemanager.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    List<Role> findByName(String name);

}
