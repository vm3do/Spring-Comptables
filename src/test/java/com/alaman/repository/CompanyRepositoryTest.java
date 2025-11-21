package com.alaman.repository;

import com.alaman.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldFindCompanyByIce_WhenCompanyExists() {
        // Arrange
        Company company = Company.builder()
                .name("Test Company")
                .ice("ICE123456789")
                .address("123 Test Street")
                .phone("0123456789")
                .email("test@company.com")
                .build();
        entityManager.persistAndFlush(company);

        // Act
        Optional<Company> foundCompany = companyRepository.findByIce("ICE123456789");

        // Assert
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getName()).isEqualTo("Test Company");
        assertThat(foundCompany.get().getIce()).isEqualTo("ICE123456789");
    }

    @Test
    void shouldReturnEmpty_WhenCompanyNotFound() {
        // Act
        Optional<Company> foundCompany = companyRepository.findByIce("NONEXISTENT");

        // Assert
        assertThat(foundCompany).isEmpty();
    }
}