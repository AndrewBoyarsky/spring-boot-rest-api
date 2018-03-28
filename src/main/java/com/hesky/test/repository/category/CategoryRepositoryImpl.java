package com.hesky.test.repository.category;

import com.hesky.test.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data Jpa Wrapper over {@link com.hesky.test.repository.category.CrudCategoryRepository} for {@link com.hesky.test.model.Category} providing additional level of encapsulation and add functionality
 */
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    @Autowired
    private CrudCategoryRepository crudRepository;

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    @Transactional
    public Category save(Category category) {
        if (!category.isNew() && get(category.getId()) == null) {
            return null;
        }
        return crudRepository.save(category);
    }

    @Override
    public Category get(int id) {
        return crudRepository.getOne(id);
    }

    @Override
    public List<Category> getAll() {
        return crudRepository.findAll();
    }

    @Override
    public List<Category> filter(String query) {
        return crudRepository.findAllByNameOrDescriptionContainingIgnoreCase(query, query);
    }
}
