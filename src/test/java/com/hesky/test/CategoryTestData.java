package com.hesky.test;

import com.hesky.test.model.Category;

public class CategoryTestData {
    public static final Category NOTEBOOKS = new Category(100_000, "Notebooks", "Laptops, notebooks, transformers", 2);
    public static final Category HARDWARE = new Category(100_001, "Computer hardware", "Video cards, processors, motherboards", 3);
    public static final Category DEFAULT = new Category(100_002, "DEFAULT_CATEGORY", "", 3);
    public static final int NOTEBOOKS_ID = 100_000;
    public static final int HARDWARE_ID = 100_001;
    public static final int DEFAULT_ID = 100_002;
}
