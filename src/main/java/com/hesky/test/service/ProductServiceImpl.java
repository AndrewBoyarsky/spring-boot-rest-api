package com.hesky.test.service;

import com.hesky.test.model.Product;
import com.hesky.test.repository.product.ProductRepository;
import com.hesky.test.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static com.hesky.test.util.ValidationUtil.checkNotFoundWithId;

/**
 * Product service implementation using {@link com.hesky.test.repository.product.ProductRepository}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }


    @Override
    public Product save(Product product, int categoryId) {
        Assert.notNull(product, "product must not be null");
        return repository.save(product, categoryId);
    }

    @Override
    public Product save(Product product) {
        Assert.notNull(product, "product must not be null");
        return repository.save(product);
    }

    @Override
    public Product update(Product product, int categoryId) throws NotFoundException {
        Assert.notNull(product, "product must not be null");
        return checkNotFoundWithId(repository.save(product, categoryId), product.getId());
    }

    @Override
    public Product update(Product product) throws NotFoundException {
        Assert.notNull(product, "product must not be null");
        return checkNotFoundWithId(repository.save(product), product.getId());

    }

    @Override
    public void delete(int id, int categoryId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, categoryId), id);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public Product get(int id, int categoryId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, categoryId), id);
    }

    @Override
    public Product get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public List<Product> getAll(int categoryId) {
        return repository.getAll(categoryId);
    }

    @Override
    public List<Product> getAll() {
        return repository.getAll();
    }

    @Override
    public Product getWithCategory(int id) {
        return checkNotFoundWithId(repository.getWithCategory(id), id);
    }
}
