package com.webshopping.WebShoppers.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Entity
public class Product {
    @Id
    private int id;
    private String name;
    private String description;
    private String price;
    private String category;
    private String image;
    private int quantity;
    private int discount;
    private int discountedPrice;

    public Product(int id, String name, String description, String price, String category, String image, int quantity, int discount, int discountedPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.quantity = quantity;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
    }
}
