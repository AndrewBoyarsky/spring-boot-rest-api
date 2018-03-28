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
import static com.hesky.test.web.rest.product.ProductWithoutCategoryRestController.REST_URL;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Rest controller for {@link com.hesky.test.model.Product} without {@link com.hesky.test.model.Category} that uses  {@link com.hesky.test.service.ProductService}
 */
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductWithoutCategoryRestController {
    //using simplified api request string
    public static final String REST_URL = "/api/products";
    private static final Logger LOG = getLogger(ProductWithoutCategoryRestController.class);
    private ProductService service;

    @Autowired
    public ProductWithoutCategoryRestController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll() {
        LOG.info("getAll Products");
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        LOG.info("delete Product with id {}", id);
        service.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> save(@RequestBody @Valid Product product) {
        LOG.info("save new Product {} without category", product);
        checkNew(product);
        Product created = service.save(product);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid Product product, @PathVariable("id") Integer id) {
        checkIdConsistent(product, id);
        LOG.info("Update {} without category", product);
        service.update(product);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable("id") int id) {
        return service.get(id);
    }
}
