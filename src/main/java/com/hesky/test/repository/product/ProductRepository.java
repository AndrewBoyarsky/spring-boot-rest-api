package com.hesky.test.repository.product;

import com.hesky.test.model.Product;

import java.util.List;
/**
 *  Crud interface for {@link com.hesky.test.model.Product} repository
 * Duplication of methods for Product with Category and without Category could be replaced with intermediary that decides which method invoke (with or without category
 */
public interface ProductRepository {
    boolean delete(int id);

    boolean delete(int id, int categoryId);

    Product save(Product product);

    Product save(Product product, int categoryId);

    Product get(int id);

    Product get(int id, int categoryId);

    List<Product> getAll();

    List<Product> getAll(int categoryId);

    Product getWithCategory(int id);
}
