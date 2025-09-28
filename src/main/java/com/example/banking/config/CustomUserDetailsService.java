package com.example.banking.config;

import com.example.banking.entity.Customer;
import com.example.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Loads the user details for Spring Security authentication by username.
     *
     * <p>This method retrieves a {@link Customer} entity from the database
     * using the provided username. If the user is not found, it throws
     * {@link UsernameNotFoundException}. Once found, it returns a Spring
     * Security {@link User} object containing the username, password,
     * and granted authorities (roles).</p>
     *
     * @param username the username of the customer to load
     * @return a Spring Security {@link UserDetails} object with credentials and roles
     * @throws UsernameNotFoundException if no customer is found with the given username
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
                customer.getUsername(),
                customer.getPassword(),
                customer.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .collect(Collectors.toList())
        );
    }
}
