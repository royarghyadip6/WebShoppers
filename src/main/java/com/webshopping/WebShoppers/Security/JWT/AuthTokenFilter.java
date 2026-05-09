package com.webshopping.WebShoppers.Security.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(AuthTokenFilter.class.getName());

    private JwtUtils jwtUtils;

    private UserDetailsService userDetailsService;

    /**
     * Constructor Injection : This constructor is used to inject the JwtUtils and UserDetailsService dependencies into the AuthTokenFilter. By using constructor injection, we can ensure that these dependencies are provided when an instance of AuthTokenFilter is created. This promotes better testability and immutability of the filter, as the dependencies are required and cannot be changed after the filter is instantiated.
     *
     * @param jwtUtils the JwtUtils component responsible for handling JWT operations such as token validation and extraction
     * @param userDetailsService the UserDetailsService component responsible for loading user details based on the username extracted from the JWT token
     */
    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**     * This method is responsible for filtering incoming HTTP requests and performing JWT authentication. It retrieves the JWT token from the request header, validates it, and if valid, extracts the username from the token. It then loads the user details using the UserDetailsService and creates an authentication token. Finally, it sets the authentication in the SecurityContext if the token is valid. If any exceptions occur during this process, they are logged, and the filter chain continues to process the request.
     *
     * @param request the HttpServletRequest object containing the incoming request data
     * @param response the HttpServletResponse object for sending responses back to the client
     * @param filterChain the FilterChain object that allows passing control to the next filter in the chain
     * @throws ServletException if an error occurs during servlet processing
     * @throws IOException if an I/O error occurs during request processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.getJwtHeader(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                var userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = jwtUtils.getAuthentication(jwt, userDetails);
                logger.info("Setting authentication for user: " + username);
                // Set the authentication in the SecurityContext
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.severe("Cannot set user authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);

    }

}
