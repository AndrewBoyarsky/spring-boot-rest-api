package com.hesky.test.web.rest.category;

import com.hesky.test.util.exception.NotFoundException;
import com.hesky.test.model.Category;
import com.hesky.test.service.CategoryService;
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

import static com.hesky.test.CategoryTestData.*;
import static com.hesky.test.JsonUtil.getJson;
import static com.hesky.test.model.BaseEntity.START_SEQ;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {CategoryRestController.class})
public class CategoryRestControllerUnitTest {
    private static final String REST_URL = CategoryRestController.REST_URL + "/";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService service;

    @Test
    public void testGetAll() throws Exception {
        List<Category> categories = Arrays.asList(NOTEBOOKS, HARDWARE);
        given(service.getAll()).willReturn(categories);
        String json = mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
        String expected = getJson(categories);
        JSONAssert.assertEquals(expected, json, false);
    }

    @Test
    public void testGet() throws Exception {
        given(service.get(NOTEBOOKS_ID)).willReturn(NOTEBOOKS);
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + NOTEBOOKS_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        String expected = getJson(NOTEBOOKS);
        JSONAssert.assertEquals(expected, json, false);
    }

    @Test
    public void testFilter() throws Exception {
        List<Category> categories = Arrays.asList(NOTEBOOKS);
        String queryString = "laptop";
        given(service.filter(queryString)).willReturn(categories);
        String json = mockMvc.perform(get(REST_URL + "filter")
                .param("query", queryString))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
        String expected = getJson(categories);
        JSONAssert.assertEquals(expected, json, false);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + HARDWARE_ID))
                .andExpect(status().isOk())
                .andDo(print());
        verify(service, times(1)).delete(HARDWARE.getId());
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
        verify(service, times(1)).update(updatedCategory);
    }

    @Test
    public void testSave() throws Exception {
        Category category = new Category("Category name", "Category description");
        Category savedCategory = new Category(category);
        savedCategory.setId(START_SEQ + 7);
        given(service.save(category)).willReturn(savedCategory);

        MvcResult mvcResult = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson(category)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        String expected = getJson(savedCategory);
        JSONAssert.assertEquals(expected, json, false);
    }


    @Test
    public void testGetNotFound() throws Exception {
        int nonExistentId = START_SEQ + 100;
        given(service.get(nonExistentId)).willThrow(new NotFoundException("Cannot find category"));
        MvcResult mvcResult = mockMvc.perform(get(REST_URL + nonExistentId))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        int nonExistentId = START_SEQ + 100;
        Category updatedCategory = new Category(NOTEBOOKS);
        updatedCategory.setName("Updated Asus vivobook 13");
        updatedCategory.setId(nonExistentId);
        doThrow(new NotFoundException("Cannot find product in category")).when(service).update(updatedCategory);
        mockMvc.perform(put(REST_URL + nonExistentId) //
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(updatedCategory)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        int noneExistentId = START_SEQ + 100;
        doThrow(new NotFoundException("Product is not found in category")).when(service).delete(noneExistentId);
        mockMvc.perform(delete(REST_URL + noneExistentId))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }
}

