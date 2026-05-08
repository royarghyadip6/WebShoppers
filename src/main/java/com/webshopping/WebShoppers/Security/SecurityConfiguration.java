package com.webshopping.WebShoppers.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

        /**
         * This method configures the security filter chain for the application. It allows all requests to the H2 console and requires authentication for any other requests. It also disables CSRF protection and frame options to allow the H2 console to function properly. Additionally, it enables HTTP Basic authentication and form-based login with default settings.
         * @param httpSecurity the HttpSecurity object used to configure the security settings.
         * @return a SecurityFilterChain object that defines the security configuration for the application.
         * @throws Exception if an error occurs while configuring the security settings.
         */
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

        /**
         * This method is used to create an in-memory user details manager with two users: User1 and Admin1.
         * User1 has the role "USER" and Admin1 has the role "ADMIN". The passwords are stored in plain text for simplicity.
         * {noop} is used to indicate that the password is stored in plain text and should not be encoded. This is useful for testing and development purposes, but it is not recommended for production environments where passwords should be securely hashed.
         *
         * @return an instance of UserDetailsManager containing the defined users.
         */

        @Bean
        UserDetailsManager userDetailsManager() {
                UserDetails user1 = User.withUsername("User1").password("{noop}Password1").roles("USER").build();
                UserDetails admin = User.withUsername("Admin1").password("{noop}Admin1").roles("ADMIN").build();
                return new InMemoryUserDetailsManager(user1,admin);
        }


}
