package com.hesky.test.repository.product;

import com.hesky.test.model.Product;
import com.hesky.test.repository.category.CrudCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data Jpa Wrapper over {@link com.hesky.test.repository.product.CrudProductRepository} for {@link com.hesky.test.model.Product} providing additional level of encapsulation, adding functionality and directly implementing {@link com.hesky.test.repository.product.ProductRepository}
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @Autowired
    private CrudProductRepository productRepository;
    @Autowired
    private CrudCategoryRepository categoryRepository;


    @Override
    public boolean delete(int id) {
        return productRepository.delete(id) != 0;
    }

    @Override
    public boolean delete(int id, int categoryId) {
        return productRepository.delete(id, categoryId) != 0;
    }

    @Transactional
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product save(Product product, int categoryId) {
        if (!product.isNew() && get(product.getId()) == null) {
            return null;
        }
        product.setCategory(categoryRepository.getOne(categoryId));
        return productRepository.save(product);
    }

    @Override
    public Product get(int id) {
        return productRepository.getOne(id);
    }

    @Override
    public Product get(int id, int categoryId) {
        return productRepository.getOne(id, categoryId);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.getAll();
    }

    @Override
    public List<Product> getAll(int categoryId) {
        return productRepository.getAll(categoryId);
    }

    @Override
    public Product getWithCategory(int id) {
        return productRepository.getWithCategory(id);
    }
}
