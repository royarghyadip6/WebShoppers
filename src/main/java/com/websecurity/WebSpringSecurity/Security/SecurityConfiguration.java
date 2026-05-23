package com.websecurity.WebSpringSecurity.Security;

import com.websecurity.WebSpringSecurity.Security.JWT.AuthEntryPointJwt;
import com.websecurity.WebSpringSecurity.Security.JWT.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    DataSource dataSource;

    private final AuthEntryPointJwt unauthorizedHandler;

    // Constructor Injection
    public SecurityConfiguration(AuthEntryPointJwt unauthorizedHandler, DataSource dataSource) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.dataSource = dataSource;
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


//    defaultSecurityFilterChain is common for both Basic Authentication (InMemoryUserDetailsManager and jdbcUserDetailsManager) and JWT Authentication, so it is commented out here and can be used as needed. The configureJwtSecurityFilterChain method is specifically configured for JWT authentication, while the defaultSecurityFilterChain method is a more general configuration that can be used for basic authentication or other types of authentication as well.

    /**
         * This method configures the security filter chain for the application. It allows all requests to the H2 console and requires authentication for any other requests. It also disables CSRF protection and frame options to allow the H2 console to function properly. Additionally, it enables HTTP Basic authentication and form-based login with default settings.
         * @param httpSecurity the HttpSecurity object used to configure the security settings.
         * @return a SecurityFilterChain object that defines the security configuration for the application.
         * @throws Exception if an error occurs while configuring the security settings.
         */
/*        @Bean
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
        }*/

//    InMemoryUserDetailsManager starts here

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

//    InMemoryUserDetailsManager ends here


//    jdbcUserDetailsManager starts here

        /**
         * This method is used to create a JdbcUserDetailsManager that retrieves user details from a database using the provided DataSource. It checks if the users "User1" and "Admin1" already exist in the database, and if not, it creates them with the specified usernames, passwords, and roles. The passwords are stored in plain text for simplicity, indicated by {noop}. This method allows for user management through a database rather than in-memory storage.
         *
         * @return an instance of JdbcUserDetailsManager configured with the specified users.
         */
/*        @Bean
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
        }*/

//    jdbcUserDetailsManager ends here



// JWT Authentication Configuration starts here

        /**
         * This method defines a bean for the UserDetailsService interface, specifically using the JdbcUserDetailsManager implementation. The JdbcUserDetailsManager retrieves user details from a database using the provided DataSource.
         *
         * @param dataSource the DataSource to use for database operations.
         * @return an instance of JdbcUserDetailsManager that can be used as a UserDetailsService in the application.
         */
        @Bean
        public UserDetailsService userDetailsService(DataSource dataSource) {
            return new JdbcUserDetailsManager(dataSource);
        }

        /**
         * This method initializes user data in the database using the provided UserDetailsService. It checks if the users "user" and "admin" already exist in the database, and if not, it creates them with the specified usernames, passwords, and roles. The passwords are encoded using the defined PasswordEncoder for security. This method is executed at application startup to ensure that the necessary user accounts are available for authentication.
         *
         * @param userDetailsService the UserDetailsService used to manage user details in the database.
         * @return a CommandLineRunner that initializes user data when the application starts.
         */
        @Bean
        public CommandLineRunner initData(UserDetailsService userDetailsService) {

            return args -> {

                JdbcUserDetailsManager manager =
                        (JdbcUserDetailsManager) userDetailsService;

                if (!manager.userExists("user")) {

                    UserDetails user1 = User.withUsername("user")
                            .password(passwordEncoder().encode("user"))
                            .roles("USER")
                            .build();

                    manager.createUser(user1);
                }

                if (!manager.userExists("admin")) {

                    UserDetails admin = User.withUsername("admin")
                            .password(passwordEncoder().encode("admin"))
                            .roles("ADMIN")
                            .build();

                    manager.createUser(admin);
                }
            };
        }

        /**
         * This method configures the security filter chain for JWT authentication. It allows all requests to the H2 console and the /signin endpoint, while requiring authentication for any other requests. It also sets the session management policy to stateless, disables CSRF protection, and configures HTTP Basic authentication with default settings. Additionally, it disables frame options to allow the H2 console to function properly and sets a custom authentication entry point for handling unauthorized access attempts. Finally, it adds the AuthTokenFilter before the UsernamePasswordAuthenticationFilter in the filter chain to handle JWT authentication.
         *
         * @param http the HttpSecurity object used to configure the security settings.
         * @param authTokenFilter the AuthTokenFilter used to handle JWT authentication.
         * @return a SecurityFilterChain object that defines the security configuration for JWT authentication.
         * @throws Exception if an error occurs while configuring the security settings.
         */
        @Bean
        SecurityFilterChain configureJwtSecurityFilterChain(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {
                http
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/signin/**").permitAll()
                                .anyRequest().authenticated())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .csrf(csrf -> csrf.disable())
                        .httpBasic(Customizer.withDefaults())
                        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                        .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                                response.setContentType("application/json");
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getOutputStream().println("{ \"status\": "+response.getStatus()+", \"message\": \"" + authException.getMessage() + "\", \"path\": \"" + request.getRequestURI() + "\" }");
                        }))
                        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
                
         return http.build();
    }

    /**
     * This method defines a bean for the AuthenticationManager, which is responsible for handling authentication processes in the application. It retrieves the AuthenticationManager from the provided AuthenticationConfiguration, allowing it to be used throughout the application for authentication purposes, such as validating user credentials during login attempts.
     * @param authenticationConfiguration the AuthenticationConfiguration used to obtain the AuthenticationManager.
     * @return an instance of AuthenticationManager that can be used for authentication processes in the application.
     * @throws Exception if an error occurs while retrieving the AuthenticationManager from the AuthenticationConfiguration.
     */
     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
         return authenticationConfiguration.getAuthenticationManager();
     }

    //        JWT Authentication Configuration ends here

}