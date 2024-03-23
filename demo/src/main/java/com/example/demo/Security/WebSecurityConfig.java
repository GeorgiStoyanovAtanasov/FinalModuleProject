package com.example.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new PlayerDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/mine").permitAll()//hasAnyAuthority("USER", "ADMIN", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/home").permitAll()//hasAnyAuthority("USER", "ADMIN", "ROLE_USER", "ROLE_ADMIN")//hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/mine/gold").permitAll()
                        .requestMatchers("/gold/mine/success").permitAll()
                        .requestMatchers("/admin/selectGoldMine").permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/mine")
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}


