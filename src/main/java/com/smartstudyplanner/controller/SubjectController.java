package com.smartstudyplanner.controller;
import com.smartstudyplanner.entity.Subject;
import com.smartstudyplanner.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired private SubjectService subjectService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> create(@PathVariable Long userId, @RequestBody Subject subject) {
        try {
            return ResponseEntity.ok(subjectService.create(userId, subject));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(subjectService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subjectService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Subject subject) {
        try {
            return ResponseEntity.ok(subjectService.update(id, subject));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
