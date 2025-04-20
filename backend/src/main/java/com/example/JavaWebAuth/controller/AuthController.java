package com.example.JavaWebAuth.controller;

import com.example.JavaWebAuth.model.User;
import com.example.JavaWebAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // allow all for demo — adjust in production
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // POST /api/auth/register
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return "Username already exists!";
        }

        userRepository.save(user);
        return "Registration successful!";
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid username or password!";
        }
    }
}
