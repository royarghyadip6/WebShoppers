package com.websecurity.WebSpringSecurity.Security.JWT;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    @Value("${spring.app.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.app.jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwt.issuer}")
    private String jwtIssuer;

    @Value("${spring.app.jwt.audience}")
    private String jwtAudience;

    @Value("${spring.app.jwt.signing-algorithm}")
    private String jwtSigningAlgorithm;

    @Value("${spring.app.jwt.token-prefix}")
    private String jwtTokenPrefix;

    @Value("${spring.app.jwt.header-name}")
    private String jwtHeaderName;


    /**     * This method retrieves the JWT token from the specified HTTP request header. It checks if the header contains a token that starts with the defined token prefix (e.g., "Bearer "). If a valid token is found, it extracts and returns the token by removing the prefix. If the header does not contain a valid token, it returns null.
     *
     * @param request the HttpServletRequest object containing the incoming request data
     * @return the extracted JWT token if present and valid; otherwise, null
     */
    public String getJwtHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtHeaderName);
        if (bearerToken != null && bearerToken.startsWith(jwtTokenPrefix)) {
            return bearerToken.substring(jwtTokenPrefix.length());
        }
        return null;
    }

    /**     * This method generates a JWT token based on the provided UserDetails object. It extracts the username from the UserDetails and calls the generateToken method to create a JWT token using the username as the subject. The generated token is then returned as a string.
     *
     * @param userDetails the UserDetails object containing user information, including the username
     * @return a JWT token string generated for the specified user
     */
    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return generateToken(username);
    }

    /**     * This method validates the provided JWT token by parsing it using the configured secret key and checking for various exceptions that may occur during parsing. It logs the validation process and returns true if the token is valid, or false if any exceptions are caught indicating an invalid token.
     *
     * @param token the JWT token string to be validated
     * @return true if the token is valid; false if the token is invalid or expired
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            logger.info("JWT token is valid: " + token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.warning("Invalid JWT signature: " + e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.warning("JWT token is expired: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            logger.warning("Invalid JWT token: " + e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            logger.warning("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warning("JWT claims string is empty: " + e.getMessage());
        }

        logger.info("Validating JWT token: " + token);
        return true;
    }

    /**     * This method generates a JWT token for the specified username. It uses the configured properties such as issuer, audience, expiration time, signing algorithm, and secret key to create a JWT token. The generated token is then returned as a string.
     *
     * @param username the username for which the JWT token is to be generated
     * @return a JWT token string generated for the specified username
     */
    private String generateToken(String username) {
        logger.info("Generating JWT token for user: " + username);
        // Implement JWT token generation logic here using the configured properties
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.forName(jwtSigningAlgorithm), jwtSecret)
                .compact();
        logger.info("Generated JWT token for user: " + username + " with token: " + token);
        return token;
    }

    /**     * This method extracts the username from the provided JWT token. It parses the token using the configured secret key and retrieves the subject (username) from the token's claims. The extracted username is then returned as a string.
     *
     * @param token the JWT token string from which the username is to be extracted
     * @return the username extracted from the JWT token
     */
    public String getUserNameFromJwtToken(String token) {
        // Implement logic to extract username from JWT token here using the configured properties
        logger.info("Extracting username from JWT token: " + token);
        String username = Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getPayload().getSubject();
        logger.info("Extracted username from JWT token: " + username);
        return username;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String jwt, UserDetails userDetails) {
        // Implement logic to create an Authentication object based on the JWT token and UserDetails here using the configured properties
        logger.info("Creating authentication object for user: " + userDetails.getUsername());
        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}
