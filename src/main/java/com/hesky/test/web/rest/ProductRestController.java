package com.hesky.test.web.rest;

import com.hesky.test.model.Product;
import com.hesky.test.service.ProductService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.hesky.test.util.ValidationUtil.checkIdConsistent;
import static com.hesky.test.util.ValidationUtil.checkNew;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Rest controller for {@link com.hesky.test.model.Product} WITH {@link com.hesky.test.model.Category} that uses  {@link com.hesky.test.service.ProductService}
 */
@RestController
@RequestMapping(value = {ProductRestController.FULL_REST_URL, ProductRestController.SHORT_REST_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductRestController {
    //    full request url, Product depends on category
    public static final String FULL_REST_URL = "/api/categories/{categoryId}/products";
    public static final String SHORT_REST_URL = "/api/products";
    private static final Logger LOG = getLogger(ProductRestController.class);
    private ProductService service;

    @Autowired
    public ProductRestController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll(@PathVariable(value = "categoryId", required = false) Optional<Integer> categoryId) {
        LOG.info("getAll for category {}", getCategoryDescription(categoryId));
        return service.getAll(getCategoryId(categoryId));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id, @PathVariable(value = "categoryId", required = false) Optional<Integer> categoryId) {
        LOG.info("delete Product with id {} from category {}", id, getCategoryDescription(categoryId));
        service.delete(id, getCategoryId(categoryId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> save(@RequestBody @Valid Product product, @PathVariable(value = "categoryId", required = false) Optional<Integer> categoryId) {
        LOG.info("save new Product {} to category {}", product, getCategoryDescription(categoryId));
        checkNew(product);
        Product created = service.save(product, getCategoryId(categoryId));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(categoryId.isPresent() ? FULL_REST_URL : SHORT_REST_URL + "/{id}")
                .buildAndExpand(categoryId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid Product product, @PathVariable("id") Integer id, @PathVariable(value = "categoryId", required = false) Optional<Integer> categoryId) {
        checkIdConsistent(product, id);
        LOG.info("Update {} and category {}", product, getCategoryDescription(categoryId));
        service.update(product, getCategoryId(categoryId));
    }

    private int getCategoryId(Optional<Integer> categoryId) {
        return categoryId.orElse(Product.DEFAULT_CATEGORY_ID);
    }

    private Object getCategoryDescription(Optional<Integer> categoryId) {
        return categoryId.isPresent() ? categoryId.get() : "DEFAULT_CATEGORY";
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Product get(@PathVariable("id") int id, @PathVariable(value = "categoryId", required = false) Optional<Integer> categoryId) {
        return service.get(id, getCategoryId(categoryId));
    }
}
