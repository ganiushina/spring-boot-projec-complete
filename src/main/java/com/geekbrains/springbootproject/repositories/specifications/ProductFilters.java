package com.geekbrains.springbootproject.repositories.specifications;

import com.geekbrains.springbootproject.entities.Product;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
public class ProductFilters {

    private Specification<Product> spec;
    private StringBuilder filterDefinition;

    public ProductFilters(String word, Double min, Double max) {
        this.spec = Specification.where(null);
        this.filterDefinition = new StringBuilder();

        if (word != null) {
            spec = spec.and(ProductSpecs.titleContains(word));
            filterDefinition.append("&word=" + word);
        }

        if (min != null) {
            spec = spec.and(ProductSpecs.priceGreaterThanOrEq(min));
            filterDefinition.append("&min=" + min);
        }
        if (max != null) {
            spec = spec.and(ProductSpecs.priceLesserThanOrEq(max));
            filterDefinition.append("&max=" + max);
        }

    }

}
