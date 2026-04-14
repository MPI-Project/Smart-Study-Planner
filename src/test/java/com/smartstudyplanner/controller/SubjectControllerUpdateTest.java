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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SubjectController — actualizare (PUT)")
class SubjectControllerUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubjectService subjectService;

    @Test
    @DisplayName("PUT /api/subjects/{id} — 200 când actualizarea reușește")
    void update_success_returnsOk() throws Exception {
        Subject requestBody = updatedPayload();
        Subject saved = updatedPayload();
        saved.setId(3L);

        when(subjectService.update(eq(3L), any(Subject.class))).thenReturn(saved);

        mockMvc.perform(put("/api/subjects/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Chimie — seminar"))
                .andExpect(jsonPath("$.requiredHours").value(25.5));
    }

    @Test
    @DisplayName("PUT /api/subjects/{id} — 400 când service aruncă excepție")
    void update_serviceThrows_returnsBadRequest() throws Exception {
        Subject requestBody = updatedPayload();

        when(subjectService.update(eq(3L), any(Subject.class)))
                .thenThrow(new Exception("Numele materiei este obligatoriu!"));

        mockMvc.perform(put("/api/subjects/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Numele materiei este obligatoriu!"));
    }

    private static Subject updatedPayload() {
        Subject s = new Subject();
        s.setName("Chimie — seminar");
        s.setStartDate(LocalDate.of(2026, 3, 1));
        s.setDeadline(LocalDate.of(2026, 7, 20));
        s.setRequiredHours(new BigDecimal("25.50"));
        s.setCompletedHours(new BigDecimal("5.00"));
        return s;
    }
}
