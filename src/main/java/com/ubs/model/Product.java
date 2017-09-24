package com.ubs.model;

public class Product {
    private String code;
    private double price = 0;
    private Promotion promotion;

    public Product() {}

    public Product(String code, double price, Promotion promotion) {
        this.code = code;
        this.price = price;
        this.promotion = promotion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return code.equals(product.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
