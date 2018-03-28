package com.hesky.test.repository.category;

import com.hesky.test.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data Jpa for {@link com.hesky.test.model.Category}
 */
@Transactional(readOnly = true) //for queries that don't affect db tables (only Reading) [select queries]
public interface CrudCategoryRepository extends JpaRepository<Category, Integer> {

    @Override
    Category save(Category category);

    @Transactional
    @Modifying
    @Query("DELETE FROM Category c WHERE c.id=:id") //using custom query returning number of deleted records
    int delete(@Param("id") int id);

    @Override
    @Query("SELECT c FROM Category c")
    List<Category> findAll();

    /**
     * filter Categories by name or description
     */
    List<Category> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description);

    @Override
    @Query("SELECT c FROM Category c WHERE c.id=:id")
    Category getOne(@Param("id") Integer id);
}
