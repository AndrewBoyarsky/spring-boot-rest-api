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

import static com.hesky.test.CategoryTestData.HARDWARE_ID;
import static com.hesky.test.CategoryTestData.NOTEBOOKS_ID;
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
public class ProductRestControllerUnitTest {
    private static final String REST_URL = ProductRestController.FULL_REST_URL + "/";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;

    @Test
    public void testGetAll() throws Exception {
        List<Product> products = Arrays.asList(NOTEBOOK1, NOTEBOOK2);
        given(service.getAll(NOTEBOOKS_ID)).willReturn(products);
        String json = mockMvc.perform(get(REST_URL, NOTEBOOKS_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
//        String expected = getJson(mapper,NOTEBOOK1, NOTEBOOK2);
        String expected = getJson(Arrays.asList(NOTEBOOK1, NOTEBOOK2));
        JSONAssert.assertEquals(expected, json, false);
    }

    @Test
    public void testGet() throws Exception {
        given(service.get(HARDWARE3.getId(), HARDWARE_ID)).willReturn(HARDWARE3);
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + HARDWARE3.getId(), HARDWARE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        String expected = getJson(HARDWARE3);
        JSONAssert.assertEquals(expected, json, false);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + HARDWARE1.getId(), HARDWARE_ID))
                .andExpect(status().isOk())
                .andDo(print());
        verify(service, times(1)).delete(HARDWARE1.getId(), HARDWARE_ID);
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
        verify(service, times(1)).update(updatedProduct, NOTEBOOKS_ID);
    }

    @Test
    public void testSave() throws Exception {
        Product product = new Product("Product name", "Product description", 123499);
        Product savedProduct = new Product(product);
        savedProduct.setId(HARDWARE3.getId() + 1);
        given(service.save(product, NOTEBOOKS_ID)).willReturn(savedProduct);

        MvcResult mvcResult = mockMvc.perform(post(REST_URL, NOTEBOOKS_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson(product)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        String expected = getJson(savedProduct);
        JSONAssert.assertEquals(expected, json, false);
    }

    @Test
    public void testGetNotFound() throws Exception {
        given(service.get(HARDWARE3.getId(), NOTEBOOKS_ID)).willThrow(new NotFoundException("Cannot find product"));
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
        given(service.update(updatedProduct, HARDWARE_ID)).willThrow(new NotFoundException("Cannot find product in category"));
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
        doThrow(new NotFoundException("Product is not found in category")).when(service).delete(noneExistentId, HARDWARE_ID);
        mockMvc.perform(delete(REST_URL + noneExistentId, HARDWARE_ID))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }
}

