package com.hesky.test.service;

import com.hesky.test.model.Product;
import com.hesky.test.repository.CategoryRepository;
import com.hesky.test.repository.ProductRepository;
import com.hesky.test.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.hesky.test.util.ValidationUtil.checkNotFoundWithId;

/**
 * Product service implementation using {@link ProductRepository}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Product save(Product product, int categoryId) {
        Assert.notNull(product, "product must not be null");
        product.setCategory(categoryRepository.getOne(categoryId));
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product, int categoryId) throws NotFoundException {
        Assert.notNull(product, "product must not be null");
        get(product.getId(), categoryId); //check product existence
        product.setCategory(categoryRepository.getOne(categoryId));
        return productRepository.save(product);
    }

    @Override
    public void delete(int id, int categoryId) throws NotFoundException {
        checkNotFoundWithId(productRepository.delete(id, categoryId) != 0, id);
    }

    @Override
    public Product get(int id, int categoryId) throws NotFoundException {
        return checkNotFoundWithId(productRepository.getOneByIdAndCategoryId(id, categoryId), id);
    }

    @Override
    public List<Product> getAll(int categoryId) {
        return productRepository.getAllByCategoryId(categoryId);
    }

    @Override
    public Product getWithCategory(int id) {
        return checkNotFoundWithId(productRepository.getWithCategory(id), id);
    }
}
