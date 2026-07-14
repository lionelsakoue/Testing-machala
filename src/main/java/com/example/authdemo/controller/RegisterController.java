package com.example.authdemo.controller;

import com.example.authdemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    /**
     * Registration persists to the database through UserService, which saves via
     * UserRepository. The previous implementation wrote to an
     * InMemoryUserDetailsManager, so accounts lived only in the JVM heap, were
     * lost on restart, and never reached the USERS table.
     */
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            model.addAttribute("error", "Username and password are required.");
            return "register";
        }

        try {
            userService.registerNewUser(username, password);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        // login.html renders a success banner when ?registered is present.
        return "redirect:/login?registered";
    }
}
