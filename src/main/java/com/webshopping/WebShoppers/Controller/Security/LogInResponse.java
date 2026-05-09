package com.webshopping.WebShoppers.Controller.Security;

import java.util.List;

public class LogInResponse {
    private String token;
    private String username; // Optional: Include username in the response
    private List<String> roles; // Optional: Include user roles in the response

    public LogInResponse() {
    }

    public LogInResponse(String token) {
        this.token = token;
    }

    public LogInResponse(String jwtToken, String username, List<String> roles) {
        this.token = jwtToken;
         this.username = username;
         this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
