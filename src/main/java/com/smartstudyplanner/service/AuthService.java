package com.smartstudyplanner.service;

import com.smartstudyplanner.entity.User;
import com.smartstudyplanner.repository.UserRepository;
import com.smartstudyplanner.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthService {
    @Autowired private UserRepository userRepository;
    @Autowired private UserValidator validator;
    @Autowired private BCryptPasswordEncoder passwordEncoder; // Injectăm encoder-ul

    public User signUp(User user) throws Exception {
        validator.validate(user.getEmail(), user.getPassword());

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Utilizatorul există deja!");
        }

        // CRIPTARE: Transformăm parola "parola123" în ceva de genul "$2a$10$..."
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    public User logIn(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Utilizator negăsit!"));

        // VERIFICARE: Nu putem folosi .equals() pentru că parola din DB e criptată
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Parolă incorectă!");
        }

        return user;
    }

}