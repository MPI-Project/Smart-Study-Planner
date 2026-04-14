package com.smartstudyplanner.controller;

import com.smartstudyplanner.entity.Subject;
import com.smartstudyplanner.service.SubjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SubjectController — citire (GET)")
class SubjectControllerReadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Test
    @DisplayName("GET /api/subjects — 200 cu listă de materii")
    void getAll_returnsOkWithSubjects() throws Exception {
        Subject s = sampleSubject(1L, "Istorie");
        when(subjectService.getAll()).thenReturn(List.of(s));

        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Istorie"));
    }

    @Test
    @DisplayName("GET /api/subjects — 200 cu listă goală")
    void getAll_returnsEmptyList() throws Exception {
        when(subjectService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/subjects/{id} — 200 când materia există")
    void getById_found_returnsOk() throws Exception {
        Subject s = sampleSubject(5L, "Fizică");
        when(subjectService.getById(5L)).thenReturn(s);

        mockMvc.perform(get("/api/subjects/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Fizică"));
    }

    @Test
    @DisplayName("GET /api/subjects/{id} — 400 când materia nu există")
    void getById_notFound_returnsBadRequest() throws Exception {
        when(subjectService.getById(99L))
                .thenThrow(new Exception("Materia nu există!"));

        mockMvc.perform(get("/api/subjects/99"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Materia nu există!"));
    }

    private static Subject sampleSubject(Long id, String name) {
        Subject s = new Subject();
        s.setId(id);
        s.setName(name);
        s.setStartDate(LocalDate.of(2026, 2, 1));
        s.setDeadline(LocalDate.of(2026, 5, 15));
        s.setRequiredHours(new BigDecimal("30.00"));
        s.setCompletedHours(BigDecimal.ZERO);
        return s;
    }
}
