package com.smartstudyplanner.dto;

import lombok.Data;

@Data // Asigură-te că ai Lombok, altfel generează Getter/Setter manual
public class LoginRequest {
    private String email;
    private String password;
}