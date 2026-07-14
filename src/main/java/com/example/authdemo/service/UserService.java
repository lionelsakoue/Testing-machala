package com.example.authdemo.service;

import com.example.authdemo.model.User;
import com.example.authdemo.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return repo.findByUsername(username).isPresent();
    }

    /**
     * Passwords are BCrypt-hashed before being written. CustomUserDetailsService
     * reads the hash back out, and DaoAuthenticationProvider compares against it
     * with the same PasswordEncoder bean.
     */
    @Transactional
    public User registerNewUser(String username, String rawPassword) {
        if (usernameExists(username)) {
            throw new IllegalArgumentException("username taken");
        }
        User user = new User(username, encoder.encode(rawPassword));
        return repo.save(user);
    }
}
