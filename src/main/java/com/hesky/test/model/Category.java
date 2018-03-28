package com.hesky.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Entity that represents a type or group for {@link com.hesky.test.model.Product}, contains many Products (but still may be without products)
 * Category should has unique name
 */
@Entity
@Table(name = "categories", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "categories_unique_name_idx")})
public class Category extends BaseEntity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnore //or use jackson Hibernate5Module
    private List<Product> products;
    /**
     * Using hibernate formula for calculating productCount where send sql query "select" to db.
     * Example of sql: select id, name, description, (select count(*) from products p where p.category_id = id) AS productCount from categories
     */
    @Formula(value = "(select count(*) from products p where p.category_id = id)")
    private int productCount;


    public Category(Category category) {
        super(category.getId(), category.getName(), category.getDescription());
        this.productCount = category.getProductCount();
    }

    public Category(Integer id, String name, String description, int productCount) {
        super(id, name, description);
        this.productCount = productCount;

    }

    public Category(String name, String description) {
        super(null, name, description);
    }

    public Category() {

    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getProductCount() {

        return productCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return getProductCount() == category.getProductCount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getProductCount());
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +"," +
                "name=\'" + getName() + "\'," +
                "description=\'" + getDescription() + "\'," +
                "productCount=" + productCount +
                '}';
    }
}
