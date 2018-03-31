package com.hesky.test.repository;

import com.hesky.test.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data Jpa for {@link com.hesky.test.model.Product}.
 */
@Transactional(readOnly = true) //for queries that don't affect db tables (only Reading) [select queries]
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.id=:id AND p.category.id=:categoryId")
    int delete(@Param("id") int id, @Param("categoryId") int categoryId);

    @Override
    @Modifying
    Product save(Product product);

    Product getOneByIdAndCategoryId(Integer id, Integer categoryId);

    List<Product> getAllByCategoryId(Integer categoryId);


    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id=?1")
    Product getWithCategory(int id);
}
