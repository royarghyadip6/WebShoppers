package com.webshopping.WebShoppers.Controller;

import com.webshopping.WebShoppers.Entity.Product;
import com.webshopping.WebShoppers.Service.ProcessProducts;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebShoppersController {

    private ProcessProducts processProducts;

    /**
     * Constructor Injection :
     * @param processProducts
     */
    public WebShoppersController(ProcessProducts processProducts) {
        this.processProducts = processProducts;
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String welcome() {
        return "Hello WebShoppers!";
    }

    @RequestMapping(value="/getAllData", method=RequestMethod.GET)
    public List<Product> getAllProducts() {
        System.out.println("Getting all products");
        return processProducts.getAllProducts();
    }

}
