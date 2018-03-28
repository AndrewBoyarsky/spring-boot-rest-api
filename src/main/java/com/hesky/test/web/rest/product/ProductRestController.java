package com.hesky.test.web.rest.product;

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

import static com.hesky.test.util.ValidationUtil.checkIdConsistent;
import static com.hesky.test.util.ValidationUtil.checkNew;
import static org.slf4j.LoggerFactory.getLogger;
/**
 * Rest controller for {@link com.hesky.test.model.Product} WITH {@link com.hesky.test.model.Category} that uses  {@link com.hesky.test.service.ProductService}
 */
@RestController
@RequestMapping(value = ProductRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductRestController {
//    full request url, Product depends on category
    public static final String REST_URL = "/api/category/{categoryId}/products";
    private static final Logger LOG = getLogger(ProductRestController.class);
    private ProductService service;

    @Autowired
    public ProductRestController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll(@PathVariable("categoryId") int categoryId) {
        LOG.info("getAll for category {}", categoryId);
        return service.getAll(categoryId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id, @PathVariable("categoryId") int categoryId) {
        LOG.info("delete Product with id {}", id);
        service.delete(id, categoryId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> save(@RequestBody @Valid Product product, @PathVariable("categoryId") int categoryId) {
        LOG.info("save new Product {} to category {}", product, categoryId);
        checkNew(product);
        Product created = service.save(product, categoryId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(categoryId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid Product product, @PathVariable("id") Integer id, @PathVariable("categoryId") Integer categoryId) {
        checkIdConsistent(product, id);
        LOG.info("Update {} and category {}", product, categoryId);
        service.update(product, categoryId);
    }

    @GetMapping(value = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public Product get(@PathVariable("id") int id, @PathVariable("categoryId") int categoryId) {
        return service.get(id, categoryId);
    }
}
