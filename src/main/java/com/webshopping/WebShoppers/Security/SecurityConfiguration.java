package com.webshopping.WebShoppers.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Bean
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
                System.out.println("Inside defaultSecurityFilterChain");
                httpSecurity.authorizeHttpRequests(
                        request -> request
                        .requestMatchers("h2-console/**").permitAll()
                        .anyRequest().authenticated())
                        .csrf(csrf -> csrf.disable())
                        .headers(header -> header.frameOptions(frame -> frame.disable()));

                httpSecurity.httpBasic(Customizer.withDefaults());
                httpSecurity.formLogin(Customizer.withDefaults());

                return httpSecurity.build();
        }

        @Bean
        UserDetailsManager userDetailsManager() {
                UserDetails user1 = User.withUsername("User1").password("{noop}Password1").roles("USER").build();
                UserDetails admin = User.withUsername("Admin1").password("{noop}Admin1").roles("ADMIN").build();
                return new InMemoryUserDetailsManager(user1,admin);
        }


}
