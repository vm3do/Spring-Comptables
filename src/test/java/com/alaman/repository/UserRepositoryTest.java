package com.alaman.repository;

import com.alaman.entity.Role;
import com.alaman.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail_WhenUserExists() {
        // Arrange
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .role(Role.COMPANY)
                .build();
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFullName()).isEqualTo("Test User");
        assertThat(foundUser.get().getRole()).isEqualTo(Role.COMPANY);
    }

    @Test
    void shouldReturnEmpty_WhenUserNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundUser).isEmpty();
    }
}