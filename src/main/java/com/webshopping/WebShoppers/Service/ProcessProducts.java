package com.webshopping.WebShoppers.Service;

import com.webshopping.WebShoppers.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessProducts {

    public List<Product> getAllProducts() {

        List<Product> productList = new ArrayList<>();

        productList.add(new Product(1, "Premium Dog Kibble", "High-protein dry food for adult dogs.", "$45.00", "Food", "dog_food.jpg", 50, 10, 40));
        productList.add(new Product(2, "Squeaky Rubber Bone", "Durable chew toy for aggressive chewers.", "$12.00", "Toys", "bone_toy.jpg", 120, 0, 12));
        productList.add(new Product(3, "Orthopedic Pet Bed", "Memory foam bed for senior pets.", "$85.00", "Bedding", "pet_bed.jpg", 15, 15, 72));
        productList.add(new Product(4, "Catnip Mouse Toy", "Interactive toy infused with organic catnip.", "$8.00", "Toys", "cat_mouse.jpg", 200, 5, 7));
        productList.add(new Product(5, "Stainless Steel Bowl", "Non-slip water or food bowl (1L).", "$15.00", "Accessories", "bowl.jpg", 80, 20, 12));
        productList.add(new Product(6, "Flea & Tick Shampoo", "Medicated shampoo for itchy skin.", "$22.00", "Health", "shampoo.jpg", 45, 10, 19));
        productList.add(new Product(7, "Retractable Leash", "16ft heavy-duty walking leash.", "$28.00", "Gear", "leash.jpg", 60, 0, 28));
        productList.add(new Product(8, "Grain-Free Cat Food", "Wet food pouches, salmon flavor.", "$35.00", "Food", "cat_food.jpg", 100, 10, 31));
        productList.add(new Product(9, "Multi-Level Cat Tree", "60-inch tower with scratching posts.", "$120.00", "Furniture", "cat_tree.jpg", 10, 25, 90));
        productList.add(new Product(10, "Grooming Brush Kit", "Set of 3 brushes for all fur types.", "$30.00", "Grooming", "brush_kit.jpg", 40, 15, 25));
        productList.add(new Product(11, "Pet First Aid Kit", "Emergency supplies for travel.", "$55.00", "Health", "safety_kit.jpg", 25, 5, 52));
        productList.add(new Product(12, "Cooling Mat", "Pressure-activated gel cooling pad.", "$40.00", "Bedding", "cool_mat.jpg", 30, 10, 36));
        productList.add(new Product(13, "Training Treats", "Low-calorie salmon flavored bites.", "$10.00", "Food", "treats.jpg", 150, 0, 10));
        productList.add(new Product(14, "Automatic Feeder", "Programmable digital pet feeder.", "$95.00", "Tech", "feeder.jpg", 12, 20, 76));
        productList.add(new Product(15, "Winter Pet Sweater", "Knitted wool sweater for small dogs.", "$25.00", "Clothing", "sweater.jpg", 55, 30, 17));
        productList.add(new Product(16, "Dental Chew Sticks", "Daily treats for oral hygiene.", "$18.00", "Health", "dental_chews.jpg", 90, 10, 16));
        productList.add(new Product(17, "Foldable Pet Crate", "Portable travel crate for cats/dogs.", "$70.00", "Gear", "pet_crate.jpg", 18, 15, 59));
        productList.add(new Product(18, "Laser Pointer Toy", "USB rechargeable exercise toy.", "$14.00", "Toys", "laser.jpg", 75, 0, 14));
        productList.add(new Product(19, "Calming Hemp Oil", "Natural anxiety relief drops.", "$32.00", "Health", "hemp_oil.jpg", 40, 12, 28));
        productList.add(new Product(20, "Microchip Scanner", "Handheld RFID reader for rescuers.", "$150.00", "Tech", "scanner.jpg", 8, 5, 142));

        // Use the list as needed
        System.out.println("Loaded " + productList.size() + " products for adoptncare.com");

        return productList;
    }
}
