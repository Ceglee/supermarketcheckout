package com.ubs;

import com.ubs.dao.ProductDAO;
import com.ubs.model.Product;
import com.ubs.model.Promotion;

import java.util.HashMap;
import java.util.Map;

public class FastShopCash extends ShopCash{
    private double sum = 0;
    private Map<String, Integer> scannedProducts = new HashMap<>();

    public FastShopCash(ProductDAO dao) {
        super(dao);
    }

    @Override
    public final double checkBill() {
        return sum;
    }

    @Override
    public final void scanProduct(String productCode) {
        Product product = dao.getByProductCode(productCode);
        if (product == null) return;

        int count = scannedProducts.getOrDefault(productCode, 0);
        scannedProducts.put(productCode, ++count);

        sum += product.getPrice();

        Promotion promotion = product.getPromotion();
        if (promotion == null) return;

        if (count % promotion.getQuantity() == 0) {
            sum -= ((product.getPrice() * promotion.getQuantity()) - promotion.getPrice());
        }
    }

    @Override
    protected final void closeBill() {
        scannedProducts = new HashMap<>();
        this.sum = 0;
    }
}
