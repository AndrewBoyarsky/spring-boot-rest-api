package com.hesky.test.service;

import com.hesky.test.model.Category;
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
import static com.hesky.test.model.BaseEntity.START_SEQ;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/data.sql"})
public class CategoryServiceTest {
    @Autowired
    private CategoryService service;

    @Test
    public void testGet() {
        Category category = service.get(NOTEBOOKS_ID);
        Assert.assertEquals(NOTEBOOKS, category);
    }

    @Test
    public void testGetAll() {
        List<Category> categories = service.getAll();
        Assert.assertEquals(Arrays.asList(NOTEBOOKS, HARDWARE, DEFAULT), categories);
    }

    @Test
    public void testFilter() {
        List<Category> categories1 = service.filter("Note");
        List<Category> categories2 = service.filter("books");
        List<Category> categories3 = service.filter("laptop");
        Assert.assertEquals(Collections.singletonList(NOTEBOOKS), categories1);
        Assert.assertEquals(Collections.singletonList(NOTEBOOKS), categories2);
        Assert.assertEquals(Collections.singletonList(NOTEBOOKS), categories3);
    }

    @Test
    public void testSave() {
        Category newItem = new Category(null, "Hardware", "hardware description", 0);
        Category savedCategory = service.save(newItem);
        Assert.assertEquals(Arrays.asList(NOTEBOOKS, HARDWARE, DEFAULT, savedCategory), service.getAll());
    }

    @Test
    public void testUpdate() {
        Category updated = new Category(HARDWARE);
        updated.setName("PC Hardware");
        service.update(updated);
        Assert.assertEquals(Arrays.asList(NOTEBOOKS, updated, DEFAULT), service.getAll());
    }

    @Test
    public void testDelete() {
        service.delete(HARDWARE_ID);
        Assert.assertEquals(Arrays.asList(NOTEBOOKS, DEFAULT), service.getAll());
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() {
        service.get(START_SEQ + 100); //get with id, which not exist
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() {
        Category fake = new Category(NOTEBOOKS);
        fake.setId(START_SEQ + 100);
        service.update(fake);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() {
        service.delete(START_SEQ + 100);
    }
}
