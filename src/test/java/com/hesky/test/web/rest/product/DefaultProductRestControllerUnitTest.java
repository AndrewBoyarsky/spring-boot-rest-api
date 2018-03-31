package com.hesky.test.web.rest.product;

import com.hesky.test.WebConfig;
import com.hesky.test.model.Product;
import com.hesky.test.service.ProductService;
import com.hesky.test.util.exception.NotFoundException;
import com.hesky.test.web.rest.ProductRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static com.hesky.test.JsonUtil.getJson;
import static com.hesky.test.ProductTestData.*;
import static com.hesky.test.model.BaseEntity.START_SEQ;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {ProductRestController.class}, excludeAutoConfiguration = {WebConfig.class})
public class DefaultProductRestControllerUnitTest {
    private static final String REST_URL = ProductRestController.SHORT_REST_URL + "/";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;

    @Test
    public void testGetAll() throws Exception {
        List<Product> products = Arrays.asList(DEFAULT_PRODUCT1, DEFAULT_PRODUCT2, DEFAULT_PRODUCT3);
        given(service.getAll(Product.DEFAULT_CATEGORY_ID)).willReturn(products);
        String actual = mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
//        String expected = getJson(mapper,NOTEBOOK1, NOTEBOOK2);
        String expected = getJson(Arrays.asList(DEFAULT_PRODUCT1, DEFAULT_PRODUCT2, DEFAULT_PRODUCT3));
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void testGet() throws Exception {
        given(service.get(DEFAULT_PRODUCT2.getId(), Product.DEFAULT_CATEGORY_ID)).willReturn(DEFAULT_PRODUCT2);
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + DEFAULT_PRODUCT2.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        String expected = getJson(DEFAULT_PRODUCT2);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + DEFAULT_PRODUCT3.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        verify(service, times(1)).delete(DEFAULT_PRODUCT3.getId(), Product.DEFAULT_CATEGORY_ID);
    }

    @Test
    public void testUpdate() throws Exception {
        Product updatedProduct = new Product(DEFAULT_PRODUCT1);
        updatedProduct.setName("Updated IPhone XI");
        mockMvc.perform(put(REST_URL + DEFAULT_PRODUCT1.getId()) //support with id and without id in object
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedProduct)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service, times(1)).update(updatedProduct, Product.DEFAULT_CATEGORY_ID);
    }

    @Test
    public void testSave() throws Exception {
        Product product = new Product("Product name", "Product description", 123499);
        Product savedProduct = new Product(product);
        savedProduct.setId(START_SEQ + 100);
        given(service.save(product, Product.DEFAULT_CATEGORY_ID)).willReturn(savedProduct);

        MvcResult mvcResult = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson(product)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        String expected = getJson(savedProduct);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void testGetNotFound() throws Exception {
        int nonExistentId = DEFAULT_PRODUCT2.getId() + 100;
        given(service.get(nonExistentId, Product.DEFAULT_CATEGORY_ID)).willThrow(new NotFoundException("Cannot find product"));
        mockMvc.perform(get(REST_URL + nonExistentId))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        Product updatedProduct = new Product(DEFAULT_PRODUCT1);
        updatedProduct.setName("Updated Canon pixma");
        updatedProduct.setId(START_SEQ + 100);
        given(service.update(updatedProduct, Product.DEFAULT_CATEGORY_ID)).willThrow(new NotFoundException("Cannot find product in category"));
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
        doThrow(new NotFoundException("Product is not found in category")).when(service).delete(noneExistentId, Product.DEFAULT_CATEGORY_ID);
        mockMvc.perform(delete(REST_URL + noneExistentId))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }
}

