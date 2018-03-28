package com.hesky.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesky.test.model.Product;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonUtil {
    private static ObjectMapper mapper = getMapper();


    public static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    public static <T> String getJson(List<Product> items) throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, items);
        final byte[] data = out.toByteArray();
        return new String(data);
    }

    public static <T> String getJson(T item) throws Exception {
        return mapper.writeValueAsString(item);
    }

    public static <T> String getJson(T... items) throws Exception {
        return getJson(Arrays.stream(items).collect(Collectors.toList()));
    }

    public static <T> T readJson(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }
    public static <T> List<T> readJsonList(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }


}
