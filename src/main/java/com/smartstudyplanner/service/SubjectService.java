package com.smartstudyplanner.service;
import com.smartstudyplanner.entity.Subject;
import com.smartstudyplanner.entity.User;
import com.smartstudyplanner.repository.SubjectRepository;
import com.smartstudyplanner.repository.UserRepository;
import com.smartstudyplanner.validation.SubjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubjectService {
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SubjectValidator validator;

    public Subject create(Long userId, Subject subject) throws Exception {
        validator.validate(subject);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Utilizator negăsit!"));

        subject.setUser(user);
        return subjectRepository.save(subject);
    }
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject getById(Long id) throws Exception {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new Exception("Materia nu există!"));
    }
}
