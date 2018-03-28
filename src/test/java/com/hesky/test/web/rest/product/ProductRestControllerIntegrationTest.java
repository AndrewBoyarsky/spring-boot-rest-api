package com.hesky.test.web.rest.product;

import com.hesky.test.model.Product;
import com.hesky.test.service.ProductService;
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

import static com.hesky.test.CategoryTestData.HARDWARE_ID;
import static com.hesky.test.CategoryTestData.NOTEBOOKS_ID;
import static com.hesky.test.JsonUtil.*;
import static com.hesky.test.ProductTestData.*;
import static com.hesky.test.model.BaseEntity.START_SEQ;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/data.sql"})
public class ProductRestControllerIntegrationTest {
    private static final String REST_URL = ProductRestController.REST_URL + "/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService service;

    @Test
    public void testGetAll() throws Exception {
        List<Product> products = Arrays.asList(NOTEBOOK1, NOTEBOOK2);
        String json = mockMvc.perform(get(REST_URL, NOTEBOOKS_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
//        String expected = getJson(mapper,NOTEBOOK1, NOTEBOOK2);
        List<Product> returned = readJsonList(json, Product.class);
        Assert.assertEquals(service.getAll(NOTEBOOKS_ID), returned);
    }

    @Test
    public void testGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + HARDWARE3.getId(), HARDWARE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Product actual = readJson(json, Product.class);
        Assert.assertEquals(HARDWARE3, actual);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + HARDWARE1.getId(), HARDWARE_ID))
                .andExpect(status().isOk())
                .andDo(print());
        Assert.assertEquals(Arrays.asList(HARDWARE2, HARDWARE3), service.getAll(HARDWARE_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Product updatedProduct = new Product(NOTEBOOK2);
        updatedProduct.setName("Updated Asus vivobook 13");
        mockMvc.perform(put(REST_URL + NOTEBOOK2.getId(), NOTEBOOKS_ID) //support with id and without id in object
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedProduct)))
                .andDo(print())
                .andExpect(status().isOk());
        Assert.assertEquals(Arrays.asList(NOTEBOOK1, updatedProduct), service.getAll(NOTEBOOKS_ID));
    }

    @Test
    public void testSave() throws Exception {
        Product actual = new Product("Product name", "Product description", 123499);
        MvcResult mvcResult = mockMvc.perform(post(REST_URL, NOTEBOOKS_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson(actual)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Product returned = readJson(json, Product.class);
        actual.setId(returned.getId());
        Assert.assertEquals(actual, returned);
        Assert.assertEquals(Arrays.asList(NOTEBOOK1, NOTEBOOK2, returned), service.getAll(NOTEBOOKS_ID));
    }

    @Test
    public void testGetNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + HARDWARE3.getId(), NOTEBOOKS_ID))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        Product updatedProduct = new Product(NOTEBOOK2);
        updatedProduct.setName("Updated Asus vivobook 13");
        updatedProduct.setId(START_SEQ + 100);
        mockMvc.perform(put(REST_URL + updatedProduct.getId(), HARDWARE_ID) //
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedProduct)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        int noneExistentId = START_SEQ + 100;
        mockMvc.perform(delete(REST_URL + noneExistentId, HARDWARE_ID))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }
}
