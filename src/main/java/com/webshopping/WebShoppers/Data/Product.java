package com.webshopping.WebShoppers.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
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

}
