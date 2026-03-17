package com.smartstudyplanner.validation;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASS_REGEX = "^(?=.*[0-9])(?=.*[a-z]).{8,}$";

    public void validate(String email, String password) throws Exception {
        if (!email.matches(EMAIL_REGEX)) throw new Exception("Email format invalid!");
        if (!password.matches(PASS_REGEX)) throw new Exception("Parola trebuie să aibă min. 8 caractere și cifre!");
    }
}