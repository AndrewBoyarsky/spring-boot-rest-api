package com.hesky.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Entity that represents Product as item of {@link com.hesky.test.model.Category} (@ManyToOne).
 * Ideally product should has Category(but it's not always mandatory)
 * Product has unique name and price in pennies.
 *
 */
@Entity
@Table(name = "products", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "products_unique_name_idx")})
public class Product extends BaseEntity {
    public static final int DEFAULT_CATEGORY_ID = 0;
    @NotNull
    @Column(name = "price", nullable = false)
    @Range //by default >= 0
    private Integer price; //number of pennies
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)

    @JsonIgnore //or use jackson Hibernate5Module
    private Category category;


    public Product() {
    }

    public Product(Integer id, String name, String description, Integer price) {
        super(id, name, description);
        this.price = price;
    }

    public Product(String name, String description, Integer price) {
        super(name, description);
        this.price = price;
    }

    public Product(Product product) {
        this(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(getPrice(), product.getPrice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getPrice());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() + " "+
                "name=\'" + getName() + "\' "+
                "description=\'" + getDescription() + "\' "+
                "price=" + price +
                '}';
    }
}
