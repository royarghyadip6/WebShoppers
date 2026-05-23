package com.websecurity.WebSpringSecurity.Controller.Security;

import com.websecurity.WebSpringSecurity.Security.JWT.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class JwtAuthController {

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    /**
     * Constructor Injection : This constructor is used to inject the AuthenticationManager and JwtUtils dependencies into the JwtAuthController. By using constructor injection, we can ensure that these dependencies are provided when an instance of JwtAuthController is created. This promotes better testability and immutability of the controller, as the dependencies are required and cannot be changed after the controller is instantiated.
     *
     * @param authenticationManager the AuthenticationManager component responsible for handling authentication processes, such as validating user credentials
     * @param jwtUtils the JwtUtils component responsible for handling JWT operations such as token generation and validation
     */
    public JwtAuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LogInRequest logInRequest) {
        // Implement your sign-in logic here (e.g., register a new user, generate JWT token)
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logInRequest.getUsername(), logInRequest.getPassword())
            );
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 401);
            errorResponse.put("error", "Unauthorized");
            errorResponse.put("message", "Invalid credentials.");
            errorResponse.put("path", "/signin");
            return ResponseEntity.status(401).body(errorResponse);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LogInResponse loginResponse = new LogInResponse(jwtToken, userDetails.getUsername(), roles);
        return ResponseEntity.ok(loginResponse);
    }


}
