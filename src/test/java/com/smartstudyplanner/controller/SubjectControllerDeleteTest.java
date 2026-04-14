package com.smartstudyplanner.controller;

import com.smartstudyplanner.service.SubjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SubjectController — ștergere (DELETE)")
class SubjectControllerDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Test
    @DisplayName("DELETE /api/subjects/{id} — 200 când ștergerea reușește")
    void delete_success_returnsOk() throws Exception {
        doNothing().when(subjectService).delete(7L);

        mockMvc.perform(delete("/api/subjects/7"))
                .andExpect(status().isOk())
                .andExpect(content().string("Materia a fost ștearsă!"));
    }

    @Test
    @DisplayName("DELETE /api/subjects/{id} — 400 când service aruncă excepție")
    void delete_serviceThrows_returnsBadRequest() throws Exception {
        doThrow(new Exception("Materia nu există!"))
                .when(subjectService)
                .delete(99L);

        mockMvc.perform(delete("/api/subjects/99"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Materia nu există!"));
    }
}
