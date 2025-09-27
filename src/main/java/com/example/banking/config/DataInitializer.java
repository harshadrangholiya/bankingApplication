package com.example.banking.config;

import com.example.banking.entity.Role;
import com.example.banking.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    /**
     * Initializes the roles in the database when the application starts.
     *
     * <p>This method is executed automatically as part of Spring Boot's
     * {@link CommandLineRunner}. It checks whether {@code ADMIN} and
     * {@code CUSTOMER} roles exist in the {@link RoleRepository}. If not,
     * they are inserted into the database.</p>
     *
     * @param roleRepository the repository used to access and save roles
     * @return a {@link CommandLineRunner} that performs the role initialization
     */

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new Role(null, "ADMIN"));
            }
            if (roleRepository.findByName("CUSTOMER").isEmpty()) {
                roleRepository.save(new Role(null, "CUSTOMER"));
            }
        };
    }
}
