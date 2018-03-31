package com.hesky.test.service;

import com.hesky.test.model.Category;
import com.hesky.test.repository.CategoryRepository;
import com.hesky.test.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.hesky.test.util.ValidationUtil.checkNotFound;
import static com.hesky.test.util.ValidationUtil.checkNotFoundWithId;

/**
 * Category service implementation using {@link CategoryRepository}.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Category save(Category category) {
        Assert.notNull(category, "category must not be null");
        return repository.save(category);
    }

    @Override
    @Transactional
    public void update(Category category) {
        Assert.notNull(category, "category must not be null");
        get(category.getId()); //check for existence
        repository.save(category);
    }


    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }


    @Override
    public Category get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.getOneById(id), id);
    }

    @Override
    public List<Category> filter(String query) {
        return checkNotFound(repository.findAllByNameOrDescriptionContainingIgnoreCase(query, query), "no results for query: " + query);
    }

    @Override
    public List<Category> getAll() {
        return repository.findAll();
    }
}
