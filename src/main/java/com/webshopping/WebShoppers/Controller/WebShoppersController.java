package com.webshopping.WebShoppers.Controller;

import com.webshopping.WebShoppers.Entity.Product;
import com.webshopping.WebShoppers.Service.ProcessProducts;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebShoppersController {

    private ProcessProducts processProducts;

    /**
     * Constructor Injection :  This constructor is used to inject the ProcessProducts service into the WebShoppersController. By using constructor injection, we can ensure that the ProcessProducts dependency is provided when an instance of WebShoppersController is created. This promotes better testability and immutability of the controller, as the dependency is required and cannot be changed after the controller is instantiated.
     * @param processProducts
     */
    public WebShoppersController(ProcessProducts processProducts) {
        this.processProducts = processProducts;
    }

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
    public List<Product> getAllProducts() {
        System.out.println("Getting all products");
        return processProducts.getAllProducts();
    }

}
