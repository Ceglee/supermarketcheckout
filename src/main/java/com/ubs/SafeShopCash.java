package com.ubs;

import com.ubs.dao.ProductDAO;
import com.ubs.model.Product;
import com.ubs.model.Promotion;

import java.util.HashMap;
import java.util.Map;

public class SafeShopCash extends ShopCash {
    private Map<String, Integer> scannedProducts = new HashMap<>();

    public SafeShopCash(ProductDAO dao) {
        super(dao);
    }

    @Override
    public final double checkBill() {
        double sum = 0;
        for (Map.Entry<String, Integer> entry: scannedProducts.entrySet()) {
            Product product = dao.getByProductCode(entry.getKey());
            Promotion promotion = product.getPromotion();
            int productCount = entry.getValue();

            sum += productCount * product.getPrice();

            if (promotion != null) {
                int promotionCount = productCount / promotion.getQuantity();
                double discount = (product.getPrice() * promotion.getQuantity()) - promotion.getPrice();
                sum -= discount * promotionCount;
            }
        }
        return sum;
    }

    @Override
    public final void scanProduct(String productCode) {
        Product product = dao.getByProductCode(productCode);
        if (product != null) {
            int count = scannedProducts.getOrDefault(productCode, 0);
            scannedProducts.put(productCode, ++count);
        }
    }

    @Override
    protected final void closeBill() {
        scannedProducts = new HashMap<>();
    }
}
