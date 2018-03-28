package com.hesky.test.web.rest.category;

import com.hesky.test.model.Category;
import com.hesky.test.service.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static com.hesky.test.CategoryTestData.*;
import static com.hesky.test.JsonUtil.*;
import static com.hesky.test.model.BaseEntity.START_SEQ;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/data.sql"})
public class CategoryRestControllerIntegrationTest {
    private static final String REST_URL = CategoryRestController.REST_URL + "/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryService service;

    @Test
    public void testGetAll() throws Exception {
        String json = mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
//        String expected = getJson(mapper,NOTEBOOK1, NOTEBOOK2);
        List<Category> returned = readJsonList(json, Category.class);
//        JSONAssert.assertEquals(expected, json, false);
        Assert.assertEquals(returned, service.getAll());
    }

    @Test
    public void testGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + NOTEBOOKS_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Category returned = readJson(json, Category.class);

        Assert.assertEquals(NOTEBOOKS, returned);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + HARDWARE_ID))
                .andExpect(status().isOk())
                .andDo(print());
        Assert.assertEquals(Arrays.asList(NOTEBOOKS), service.getAll());
    }

    @Test
    public void testUpdate() throws Exception {
        Category updatedCategory = new Category(NOTEBOOKS);
        updatedCategory.setName("Updated Notebooks");
        mockMvc.perform(put(REST_URL + NOTEBOOKS_ID) //support with id and without id in category
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedCategory)))
                .andDo(print())
                .andExpect(status().isOk());
        Assert.assertEquals(Arrays.asList(updatedCategory, HARDWARE), service.getAll());
    }

    @Test
    public void testSave() throws Exception {
        Category actual = new Category("Category name", "Category description");

        MvcResult mvcResult = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson(actual)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Category returned = readJson(json, Category.class);
        actual.setId(returned.getId());
        Assert.assertEquals(actual, returned);
        Assert.assertEquals(Arrays.asList(NOTEBOOKS, HARDWARE, returned), service.getAll());
    }

    @Test
    public void testFilter() throws Exception {
        List<Category> categories = Arrays.asList(NOTEBOOKS);
        String queryString = "laptop";
        String json = mockMvc.perform(get(REST_URL + "filter")
                .param("query", queryString))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
        List<Category> returned = readJsonList(json, Category.class);
        Assert.assertEquals(categories, returned);
    }

    @Test
    public void testGetNotFound() throws Exception {
        int nonExistentId = START_SEQ + 100;
        mockMvc.perform(get(REST_URL + nonExistentId))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        int nonExistentId = START_SEQ + 100;
        Category updatedCategory = new Category(NOTEBOOKS);
        updatedCategory.setName("Updated Asus vivobook 13");
        updatedCategory.setId(nonExistentId);
        mockMvc.perform(put(REST_URL + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedCategory)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        int noneExistentId = START_SEQ + 100;
        mockMvc.perform(delete(REST_URL + noneExistentId))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }
}
