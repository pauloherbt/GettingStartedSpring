package com.cashcard.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests().requestMatchers("/cashcards/**")
                        .hasAnyRole("CARD-OWNER","NO-CARD-OWNER").and().csrf().disable().httpBasic();
        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }
    @Bean
    public UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder){
        User.UserBuilder users = User.builder();
        UserDetails sarah = users.username("sarah1")
                .password(passwordEncoder.encode("abc123"))
                .roles("CARD-OWNER").build();
        UserDetails hank = users.username("hank-no-card")
                .password(passwordEncoder.encode("abc123"))
                .roles("NO-CARD-OWNER").build();

        return new InMemoryUserDetailsManager(sarah,hank);
    }


}
