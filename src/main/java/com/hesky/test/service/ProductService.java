package com.hesky.test.service;

import com.hesky.test.model.Product;
import com.hesky.test.util.exception.NotFoundException;

import java.util.List;

/**
 * Service interface for CRUD operations with {@link com.hesky.test.model.Product}.
 * <br><br>
 * MethodsParameters:<br><br>
 * id - id field of existing {@link com.hesky.test.model.Product}
 * <br><br>
 * categoryId - id field of existing {@link com.hesky.test.model.Category} which is associated or will be associated with {@link com.hesky.test.model.Product}
 * <br><br>
 * product - {@link com.hesky.test.model.Product} object for persisting
 * <br><br>
 * {@link com.hesky.test.util.exception.NotFoundException} - thrown when Product with id doesn't found or when Product with id and associated Category (categoryId) doesn't found<br><br>
 */
public interface ProductService {

    Product save(Product product, int categoryId);

    Product update(Product product, int categoryId) throws NotFoundException;

    void delete(int id, int categoryId) throws NotFoundException;

    Product get(int id, int categoryId) throws NotFoundException;

    List<Product> getAll(int categoryId);

    Product getWithCategory(int id);
}
