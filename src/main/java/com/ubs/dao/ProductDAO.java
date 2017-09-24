package com.ubs.dao;

import com.ubs.model.Product;
import com.ubs.model.Promotion;

import java.util.HashSet;
import java.util.Set;

public class ProductDAO {
    private Set<Product> products = new HashSet<>();

    public ProductDAO() {
        products.add(new Product("A", 40.0, new Promotion(3, 70.0)));
        products.add(new Product("B", 10.0, new Promotion(2, 15.0)));
        products.add(new Product("C", 30.0, null));
        products.add(new Product("D", 25.0, null));
    }

    public Product getByProductCode(String productCode) {
        if (productCode == null) return null;
        return products
                .parallelStream()
                .filter(product -> product.getCode().equals(productCode))
                .findFirst()
                .orElse(null);
    }
}
