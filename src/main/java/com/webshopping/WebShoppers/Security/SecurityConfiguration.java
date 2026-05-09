package com.webshopping.WebShoppers.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    DataSource dataSource;

/*    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }*/

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

/*        @Bean
        UserDetailsManager userDetailsManager() {
                UserDetails user1 = User.withUsername("User1").password("{noop}Password1").roles("USER").build();
                UserDetails admin = User.withUsername("Admin1").password("{noop}Admin1").roles("ADMIN").build();
                return new InMemoryUserDetailsManager(user1,admin);
        }*/

        /**
         * This method is used to create a JdbcUserDetailsManager that retrieves user details from a database using the provided DataSource. It checks if the users "User1" and "Admin1" already exist in the database, and if not, it creates them with the specified usernames, passwords, and roles. The passwords are stored in plain text for simplicity, indicated by {noop}. This method allows for user management through a database rather than in-memory storage.
         *
         * @return an instance of JdbcUserDetailsManager configured with the specified users.
         */
        @Bean
        UserDetailsManager jdbcUserDetailsManager() {
            UserDetails user1 = User.withUsername("User").password(passwordEncoder().encode("User")).roles("USER").build();
            UserDetails admin = User.withUsername("Admin").password(passwordEncoder().encode("Admin")).roles("ADMIN").build();

            JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
            if (!jdbcUserDetailsManager.userExists(user1.getUsername())) {
                jdbcUserDetailsManager.createUser(user1);
            }
            if (!jdbcUserDetailsManager.userExists(admin.getUsername())) {
                jdbcUserDetailsManager.createUser(admin);
            }

            return jdbcUserDetailsManager;
        }

        /**
         * This method defines a bean for the PasswordEncoder interface, specifically using the BCryptPasswordEncoder implementation. The BCryptPasswordEncoder is a widely used password hashing algorithm that provides strong security for storing passwords. By defining this bean, it can be used throughout the application to encode and verify passwords securely.
         *
         * @return an instance of BCryptPasswordEncoder that can be used as a PasswordEncoder in the application.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


}
