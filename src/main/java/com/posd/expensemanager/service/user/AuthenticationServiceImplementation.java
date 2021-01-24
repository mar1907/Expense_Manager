package com.posd.expensemanager.service.user;

import com.posd.expensemanager.model.Role;
import com.posd.expensemanager.model.User;
import com.posd.expensemanager.repository.RoleRepository;
import com.posd.expensemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Service
public class AuthenticationServiceImplementation implements AuthenticationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AuthenticationServiceImplementation(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Boolean register(String username, String password) {
        if (!userRepository.findByUsername(username).isEmpty())
        {
            return false;
        }

        Role userRole = roleRepository.findByName("user").get(0);

        User user = new User(username, encodePassword(password), userRole);

        userRepository.save(user);

        return true;
    }

    @Override
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, encodePassword(password));
    }

    @Override
    public String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
