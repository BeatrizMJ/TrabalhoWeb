package com.example.productcategory.security;

import com.example.productcategory.model.User;
import com.example.productcategory.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/products/**", "/categories/**").hasAnyRole("USER", "ADMIN") 
                .requestMatchers(HttpMethod.POST, "/products/**", "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/products/**", "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/products/**", "/categories/**").hasRole("ADMIN")
                .requestMatchers("/users/**").hasRole("ADMIN") // Gerenciamento de usuários
                .anyRequest().authenticated()
            )
            .httpBasic(); // Usando autenticação HTTP Basic
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Criação do admin na memória
        InMemoryUserDetailsManager adminManager = new InMemoryUserDetailsManager();
        adminManager.createUser(org.springframework.security.core.userdetails.User.withUsername("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN").build());

        return username -> {
            if (username.equals("admin")) {
                return adminManager.loadUserByUsername("admin");
            }

            // Carregar usuários do banco de dados
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com username: " + username));

            return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword()) // Senha já está codificada
                .roles(user.getRole().toUpperCase()) // O role deve ser 'USER' ou 'ADMIN'
                .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
