package com.hesky.test;

import com.hesky.test.model.Product;

import static com.hesky.test.model.BaseEntity.START_SEQ;

public class ProductTestData {
    public static final int PRODUCT_ID = START_SEQ + 3;

    public static final Product NOTEBOOK1 = new Product(PRODUCT_ID, "Lenovo Ideapad 520", "Business laptop with Windows 10", 73999);
    public static final Product NOTEBOOK2 = new Product(PRODUCT_ID + 1, "Acer Swift 3", "Light laptop for office work", 61299);
    public static final Product HARDWARE1 = new Product(PRODUCT_ID + 2, "Asus GTX 1060 6gb", "Powerful video adapter for 1080p", 54999);
    public static final Product HARDWARE2 = new Product(PRODUCT_ID + 3, "MSI x299 Tomahawk", "Ultimate motherboard for overclockers", 40579);
    public static final Product HARDWARE3 = new Product(PRODUCT_ID + 4, "Intel Core i5-8600", "Hexa-core processor with turbo boost", 18145);

    public static final Product DEFAULT_PRODUCT1 = new Product(PRODUCT_ID + 5, "Iphone X", "New smartphone from Apple", 120599);
    public static final Product DEFAULT_PRODUCT2 = new Product(PRODUCT_ID + 6, "D-link Dir-300", "Router", 2349);
    public static final Product DEFAULT_PRODUCT3 = new Product(PRODUCT_ID + 7, "Canon Pixma mp340", "Printer, scanner, xerox", 10279);
}
