package com.example.banking.config;

import com.example.banking.entity.Role;
import com.example.banking.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

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
