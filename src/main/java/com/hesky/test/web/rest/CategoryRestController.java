package com.hesky.test.web.rest;

import com.hesky.test.model.Category;
import com.hesky.test.service.CategoryService;
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
 * Rest controller for {@link com.hesky.test.model.Category} that uses  {@link com.hesky.test.service.CategoryService}
 */
@RestController
@RequestMapping(value = CategoryRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryRestController {
//    request url
    public static final String REST_URL = "/api/categories";
    private static final Logger LOG = getLogger(CategoryRestController.class);
    private CategoryService service;

    @Autowired
    public CategoryRestController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Category> getAll() {
        LOG.info("getAll categories");
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        LOG.info("delete Category with id {}", id);
        service.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> save(@RequestBody @Valid Category category) {
        LOG.info("save new Category {}", category);
        checkNew(category);
        Category created = service.save(category);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid Category category, @PathVariable("id") Integer id) {
        checkIdConsistent(category, id);
        LOG.info("Update category {}", category);
        service.update(category);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Category get(@PathVariable("id") int id) {
        LOG.info("get Category with id {}", id);
        return service.get(id);
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> filter(@RequestParam("query") String query) {
        LOG.info("Filter categories by query: {}", query);
        return service.filter(query);
    }
}

