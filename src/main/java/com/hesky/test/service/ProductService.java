package com.hesky.test.service;

import com.hesky.test.model.Product;
import com.hesky.test.util.exception.NotFoundException;

import java.util.List;

/**
 * Service interface for CRUD operations with {@link com.hesky.test.model.Product}.
 *  <br><br>
 * MethodsParameters:<br><br>
 * id - id field of existing {@link com.hesky.test.model.Product}
 * <br><br>
 * categoryId - id field of existing {@link com.hesky.test.model.Category} which is associated or will be associated with {@link com.hesky.test.model.Product}
 * <br><br>
 * product - {@link com.hesky.test.model.Product} object for persisting
 * <br><br>
 * {@link com.hesky.test.util.exception.NotFoundException} - thrown when Product with id doesn't found or when Product with id and associated Category (categoryId) doesn't found<br><br>
 * Method duplications are used for {@link com.hesky.test.model.Product} with OR without {@link com.hesky.test.model.Category}
 */
public interface ProductService {

    Product save(Product product, int categoryId);

    //    save Product without category
    Product save(Product product);

    Product update(Product product, int categoryId) throws NotFoundException;

    //    update Product without category
    Product update(Product product) throws NotFoundException;

    void delete(int id, int categoryId) throws NotFoundException;

    //    delete Product without category
    void delete(int id) throws NotFoundException;

    Product get(int id, int categoryId) throws NotFoundException;

    //    get Product without category
    Product get(int id) throws NotFoundException;

    List<Product> getAll(int categoryId);

    //    get all Products for all categories
    List<Product> getAll();
//    could be useful to retrieve Product with Category instead of Product with null lazy category
    Product getWithCategory(int id);
}
