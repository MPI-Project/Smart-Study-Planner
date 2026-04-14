package com.smartstudyplanner.validation;

import com.smartstudyplanner.entity.Subject;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class SubjectValidator {
    public void validate(Subject subject) throws Exception {
        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            throw new Exception("Numele materiei este obligatoriu!");
        }
        if (subject.getDeadline().isBefore(subject.getStartDate())) {
            throw new Exception("Deadline-ul nu poate fi înaintea datei de început!");
        }
        if (subject.getRequiredHours().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Numărul de ore necesare trebuie să fie pozitiv!");
        }
    }
}