package com.hesky.test.repository.product;

import com.hesky.test.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Spring Data Jpa for {@link com.hesky.test.model.Product}.
 * Method duplications are used for {@link com.hesky.test.model.Product} with OR without {@link com.hesky.test.model.Category}
 */
@Transactional(readOnly = true) //for queries that don't affect db tables (only Reading) [select queries]
public interface CrudProductRepository extends JpaRepository<Product, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.id=:id")
    int delete(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.id=:id AND p.category.id=:categoryId")
    int delete(@Param("id") int id, @Param("categoryId") int categoryId);

    @Override
    Product save(Product product);

    @Override
    @Query("SELECT p FROM Product p WHERE p.id=?1")
    Product getOne(Integer id);

    @Query("SELECT p FROM Product p WHERE p.id=?1 AND p.category.id=?2")
    Product getOne(Integer id, Integer categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.id =?1")
    List<Product> getAll(int categoryId);

    @Query("SELECT p FROM Product p")
    List<Product> getAll();

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id=?1")
    Product getWithCategory(int id);
}
