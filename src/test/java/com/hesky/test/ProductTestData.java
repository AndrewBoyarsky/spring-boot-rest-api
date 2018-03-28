package com.hesky.test;

import com.hesky.test.model.Product;

import static com.hesky.test.model.BaseEntity.START_SEQ;

public class ProductTestData {
    public static final int PRODUCT_ID = START_SEQ + 2;
    public static final Product NOTEBOOK1 = new Product(PRODUCT_ID, "Lenovo Ideapad 520", "Business laptop with Windows 10", 73999);
    public static final Product NOTEBOOK2 = new Product(PRODUCT_ID + 1, "Acer Swift 3", "Light laptop for office work", 61299);
    public static final Product HARDWARE1 = new Product(PRODUCT_ID + 2, "Asus GTX 1060 6gb", "Powerful video adapter for 1080p", 54999);
    public static final Product HARDWARE2 = new Product(PRODUCT_ID + 3, "MSI x299 Tomahawk", "Ultimate motherboard for overclockers", 40579);
    public static final Product HARDWARE3 = new Product(PRODUCT_ID + 4, "Intel Core i5-8600", "Hexa-core processor with turbo boost", 18145);

}
