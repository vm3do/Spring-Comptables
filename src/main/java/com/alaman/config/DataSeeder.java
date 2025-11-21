package com.alaman.config;

import com.alaman.entity.Company;
import com.alaman.entity.Role;
import com.alaman.entity.User;
import com.alaman.repository.CompanyRepository;
import com.alaman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Create test company
            Company company = Company.builder()
                    .name("Test Company")
                    .ice("ICE123456789")
                    .address("123 Test Street")
                    .phone("0123456789")
                    .email("company@test.com")
                    .build();
            companyRepository.save(company);

            // Create company user
            User companyUser = User.builder()
                    .email("company@test.com")
                    .password(passwordEncoder.encode("password"))
                    .fullName("Company User")
                    .role(Role.COMPANY)
                    .company(company)
                    .build();
            userRepository.save(companyUser);

            // Create accountant user
            User accountant = User.builder()
                    .email("accountant@test.com")
                    .password(passwordEncoder.encode("password"))
                    .fullName("Accountant User")
                    .role(Role.ACCOUNTANT)
                    .build();
            userRepository.save(accountant);
        }
    }
}