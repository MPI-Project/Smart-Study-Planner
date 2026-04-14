package com.smartstudyplanner.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "subjects")
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Leagă materia de utilizatorul care a creat-o
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Prevents infinite loop in Postman!
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(name = "required_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal requiredHours;

    @Column(name = "completed_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal completedHours = BigDecimal.ZERO;


}