package com.smartstudyplanner.repository;

import com.smartstudyplanner.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("UserRepository / conexiune DB")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("salvează și găsește user după email")
    void findByEmail_afterSave_returnsUser() {
        User user = new User();
        user.setEmail("repo@test.com");
        user.setFirstName("Repo");
        user.setLastName("Test");
        user.setPassword("hashed");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail("repo@test.com");

        assertTrue(found.isPresent());
        assertEquals("repo@test.com", found.get().getEmail());
        assertEquals("Repo", found.get().getFirstName());
    }

    @Test
    @DisplayName("findByEmail returnează empty când email nu există")
    void findByEmail_notFound_returnsEmpty() {
        Optional<User> found = userRepository.findByEmail("nobody@example.com");
        assertTrue(found.isEmpty());
    }
}
