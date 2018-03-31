package com.hesky.test.service;

import com.hesky.test.model.Product;
import com.hesky.test.util.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.hesky.test.CategoryTestData.*;
import static com.hesky.test.ProductTestData.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/data.sql"})
public class ProductServiceTest {
    @Autowired
    private ProductService service;

    @Test
    public void testGetAll() {
        List<Product> notebooks = service.getAll(NOTEBOOKS_ID);
        Assert.assertEquals(Arrays.asList(NOTEBOOK1, NOTEBOOK2), notebooks);
        List<Product> hardwareList = service.getAll(HARDWARE_ID);
        Assert.assertEquals(Arrays.asList(HARDWARE1, HARDWARE2, HARDWARE3), hardwareList);
        List<Product> defaultProducts = service.getAll(DEFAULT_ID);
        Assert.assertEquals(Arrays.asList(DEFAULT_PRODUCT1, DEFAULT_PRODUCT2, DEFAULT_PRODUCT3), defaultProducts);
    }

    @Test
    public void testGet() {
        Product product = service.get(NOTEBOOK1.getId(), NOTEBOOKS_ID);
        Assert.assertEquals(NOTEBOOK1, product);
    }

    @Test
    public void testDelete() {
        service.delete(NOTEBOOK2.getId(), NOTEBOOKS_ID);
        Assert.assertEquals(Collections.singletonList(NOTEBOOK1), service.getAll(NOTEBOOKS_ID));
        service.delete(DEFAULT_PRODUCT2.getId(), DEFAULT_ID);
        Assert.assertEquals(Arrays.asList(DEFAULT_PRODUCT1, DEFAULT_PRODUCT3), service.getAll(DEFAULT_ID));
    }

    @Test
    public void testSave() {
        Product newNotebook = new Product("Asus Vivobook 14", "none", 61799);
        Product savedNotebook = service.save(newNotebook, NOTEBOOKS_ID);
        Assert.assertEquals(Arrays.asList(NOTEBOOK1, NOTEBOOK2, savedNotebook), service.getAll(NOTEBOOKS_ID));
    }

    @Test
    public void testUpdate() {
        Product product = new Product(HARDWARE3);
        product.setPrice(35999);
        product.setName("Intel core i7-8700K");
        Product updated = service.update(product, HARDWARE_ID);
        Assert.assertEquals(Arrays.asList(HARDWARE1, HARDWARE2, updated), service.getAll(HARDWARE_ID));
    }

    @Test
    public void testGetWithCategory() {
        Product actual = service.getWithCategory(NOTEBOOK1.getId());
        Product expected = new Product(NOTEBOOK1);
        expected.setCategory(NOTEBOOKS);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(NOTEBOOKS, actual.getCategory());
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() {
        service.get(PRODUCT_ID + 100, HARDWARE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() {
        Product product = new Product(NOTEBOOK1);
        product.setId(PRODUCT_ID + 100);
        service.update(product, NOTEBOOKS_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() {
        service.delete(PRODUCT_ID + 100, NOTEBOOKS_ID);
    }

}
