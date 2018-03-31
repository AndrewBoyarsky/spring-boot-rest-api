package com.hesky.test.web.rest.product;

import com.hesky.test.model.Product;
import com.hesky.test.service.ProductService;
import com.hesky.test.web.rest.ProductRestController;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.hesky.test.CategoryTestData.DEFAULT_ID;
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
public class DefaultProductRestControllerIntegrationTest {
    private static final String REST_URL = ProductRestController.SHORT_REST_URL + "/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService service;

    @Test
    public void testGetAll() throws Exception {
        String json = mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
//        String expected = getJson(mapper,NOTEBOOK1, NOTEBOOK2);
        List<Product> returned = readJsonList(json, Product.class);
        Assert.assertEquals(service.getAll(DEFAULT_ID), returned);
    }

    @Test
    public void testGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + DEFAULT_PRODUCT2.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Product actual = readJson(json, Product.class);
        Assert.assertEquals(DEFAULT_PRODUCT2, actual);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + DEFAULT_PRODUCT3.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        Assert.assertEquals(Arrays.asList(DEFAULT_PRODUCT1, DEFAULT_PRODUCT2), service.getAll(DEFAULT_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Product updatedProduct = new Product(DEFAULT_PRODUCT2);
        updatedProduct.setName("Updated");
        mockMvc.perform(put(REST_URL + DEFAULT_PRODUCT2.getId()) //support with id and without id in object
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedProduct)))
                .andDo(print())
                .andExpect(status().isOk());
        Assert.assertEquals(Arrays.asList(DEFAULT_PRODUCT1, updatedProduct, DEFAULT_PRODUCT3), service.getAll(DEFAULT_ID).stream().sorted(Comparator
                .comparing(Product::getId)).collect(Collectors.toList()));
    }

    @Test
    public void testSave() throws Exception {
        Product actual = new Product("Product name", "Product description", 123499);
        MvcResult mvcResult = mockMvc.perform(post(REST_URL)
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
        Assert.assertEquals(Arrays.asList(DEFAULT_PRODUCT1, DEFAULT_PRODUCT2, DEFAULT_PRODUCT3, returned), service.getAll(DEFAULT_ID));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + (START_SEQ + 100)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        Product updatedProduct = new Product(DEFAULT_PRODUCT2);
        updatedProduct.setName("Updated");
        updatedProduct.setId(START_SEQ + 100);
        mockMvc.perform(put(REST_URL + updatedProduct.getId()) //
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedProduct)))
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
