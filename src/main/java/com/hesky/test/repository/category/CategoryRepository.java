package com.hesky.test.repository.category;

import com.hesky.test.model.Category;

import java.util.List;

/**
 * Crud interface for {@link com.hesky.test.model.Category} repository
 */
public interface CategoryRepository {
    boolean delete(int id);

    Category save(Category category);

    Category get(int id);

    List<Category> getAll();

    List<Category> filter(String query);

}
