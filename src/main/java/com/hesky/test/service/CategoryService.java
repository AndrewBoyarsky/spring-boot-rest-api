package com.hesky.test.service;

import com.hesky.test.util.exception.NotFoundException;
import com.hesky.test.model.Category;

import java.util.List;
/**
 * Service interface for CRUD operations with {@link com.hesky.test.model.Category}.
 <br><br>
 MethodsParameters:<br><br>
 id - id field of existing {@link com.hesky.test.model.Category}<br><br>
 category - {@link com.hesky.test.model.Category} object for persisting<br><br>
 query - search query for filtering {@link com.hesky.test.model.Category} by name or description<br><br>
 {@link com.hesky.test.util.exception.NotFoundException} - throws when {@link com.hesky.test.model.Category} with id doesn't exist
 */
public interface CategoryService {
    Category save(Category category);

    void update(Category category) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    Category get(int id) throws NotFoundException;

    List<Category> filter(String query);

    List<Category> getAll();
}
