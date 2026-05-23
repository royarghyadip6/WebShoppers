package com.websecurity.WebSpringSecurity.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebShoppersController {

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Only users with the USER or ADMIN role can access this endpoint
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String welcome() {
        return "Hello All";
    }

    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this endpoint
    @GetMapping("/admin")
    public String welcomeAdmin() {
        return "Hello Admin!";
    }

    @PreAuthorize("hasRole('USER')") // Only users with the USER role can access this endpoint
    @GetMapping("/user")
    public String welcomeUser() {
        return "Hello User!";
    }

    @RequestMapping(value="/getAllData", method=RequestMethod.GET)
    public String getAllProducts() {
        System.out.println("Getting all products");
        return "Getting all products";
    }

}
