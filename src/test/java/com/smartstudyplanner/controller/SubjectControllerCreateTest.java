package com.smartstudyplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartstudyplanner.entity.Subject;
import com.smartstudyplanner.service.SubjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SubjectController — creare (POST)")
class SubjectControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubjectService subjectService;

    @Test
    @DisplayName("POST /api/subjects/user/{userId} — 200 când crearea reușește")
    void create_success_returnsOk() throws Exception {
        Subject body = validSubjectPayload();
        Subject saved = validSubjectPayload();
        saved.setId(1L);

        when(subjectService.create(eq(1L), any(Subject.class))).thenReturn(saved);

        mockMvc.perform(post("/api/subjects/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Matematică"));
    }

    @Test
    @DisplayName("POST /api/subjects/user/{userId} — 400 când service aruncă excepție")
    void create_serviceThrows_returnsBadRequest() throws Exception {
        Subject body = validSubjectPayload();

        when(subjectService.create(eq(1L), any(Subject.class)))
                .thenThrow(new Exception("Utilizator negăsit!"));

        mockMvc.perform(post("/api/subjects/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Utilizator negăsit!"));
    }

    private static Subject validSubjectPayload() {
        Subject s = new Subject();
        s.setName("Matematică");
        s.setStartDate(LocalDate.of(2026, 1, 10));
        s.setDeadline(LocalDate.of(2026, 6, 1));
        s.setRequiredHours(new BigDecimal("40.00"));
        s.setCompletedHours(BigDecimal.ZERO);
        return s;
    }
}
