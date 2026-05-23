package com.websecurity.WebSpringSecurity.Security.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = Logger.getLogger(AuthEntryPointJwt.class.getName());

     /**
      * This method is responsible for handling unauthorized access attempts to protected resources. When an unauthenticated user tries to access a resource that requires authentication, this method is invoked. It logs the unauthorized access attempt and sends an HTTP 401 Unauthorized error response back to the client, along with a message indicating that the user is unauthorized.
      *
      * @param request the HttpServletRequest object containing the incoming request data
      * @param response the HttpServletResponse object for sending responses back to the client
      * @param authException the AuthenticationException object containing details about the authentication failure
      * @throws IOException if an I/O error occurs during response processing
      */
     public void commence(HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException) throws IOException {
         logger.severe("Unauthorized error: " + authException.getMessage());
         response.setContentType("application/json");
         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");

         final Map<String, Object> body = new HashMap<>();
         body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
         body.put("error", "Unauthorized");
         body.put("message", authException.getMessage());
         body.put("path", request.getServletPath());

         final ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.writeValue(response.getOutputStream(), body);
     }
}
