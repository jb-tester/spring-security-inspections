package com.mytests.spring.springsecurityinspections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/","/home").permitAll()
                        .requestMatchers(HttpMethod.POST, "/home").hasRole("USER") // will be ignored, show warning!
                        .requestMatchers(HttpMethod.GET, "/test1/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/test1/test2").hasRole("ADMIN") // will be ignored, show warning!
                        //.anyRequest().authenticated()  // should be at the last position: show error
                        .requestMatchers(HttpMethod.GET, "/test2/{*var}").hasRole("MASTER") // not all matching endpoints are found: {*..} == **, not *
                        .requestMatchers(HttpMethod.GET, "/{*var}/test3").hasRole("GUEST") // not all matching endpoints are found: {*..} == **, not *
                        //.requestMatchers(HttpMethod.GET, "/aaa/{*var}/test3").hasRole("USER") // show error! - ok
                        .anyRequest().denyAll()
                );
               // .formLogin(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER").build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN").build();
        UserDetails master = User.withDefaultPasswordEncoder()
                .username("master")
                .password("master")
                .roles("MASTER").build();
        UserDetails superuser = User.withDefaultPasswordEncoder()
                .username("super")
                .password("super")
                .roles("ADMIN", "USER").build();
        UserDetails guest = User.withDefaultPasswordEncoder()
                .username("guest")
                .password("guest")
                .roles("GUEST")
                .build();
        UserDetails custom = User.withDefaultPasswordEncoder()
                .username("custom")
                .password("custom")
                .authorities("FOO", "BAR")
                .build();
        return new InMemoryUserDetailsManager(user, admin, guest, master, superuser, custom);
    }
}