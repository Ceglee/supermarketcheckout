package com.ubs;

import com.ubs.dao.ProductDAO;
import com.ubs.model.Product;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SafeShopCashTest {
    private final static ProductDAO dao = new ProductDAO();

    @Test
    public void scanProduct_onlyOneProductType_withPromotion_shouldCalculateValidSum() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode = "A";
        Product product = dao.getByProductCode(productCode);

        double price = product.getPrice();
        double promoPrice = product.getPromotion().getPrice();

        shopCash.scanProduct(productCode);
        Assert.assertEquals(price, shopCash.checkBill(), 0); // 1 product -> 0 promotion

        shopCash.scanProduct(productCode);
        shopCash.scanProduct(productCode);
        Assert.assertEquals(promoPrice, shopCash.checkBill(), 0); // 3 products -> 1 promotion

        shopCash.scanProduct(productCode);
        Assert.assertEquals(promoPrice + price, shopCash.checkBill(), 0); // 4 products -> 1 promotion

        shopCash.scanProduct(productCode);
        shopCash.scanProduct(productCode);
        Assert.assertEquals(promoPrice + promoPrice, shopCash.checkBill(), 0); // 6 products -> 2 promotion

        shopCash.scanProduct(productCode);
        Assert.assertEquals(promoPrice + promoPrice + price, shopCash.checkBill(), 0); // 7 products -> 2 promotion
    }

    @Test
    public void scanProduct_onlyOneProductType_withoutPromotion_shouldCalculateValidSum() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode = "D";
        Product product = dao.getByProductCode(productCode);

        double price = product.getPrice();

        shopCash.scanProduct(productCode);
        Assert.assertEquals(price, shopCash.checkBill(), 0); // 1 product -> 0 promotion

        shopCash.scanProduct(productCode);
        shopCash.scanProduct(productCode);
        shopCash.scanProduct(productCode);
        Assert.assertEquals(price + price + price + price, shopCash.checkBill(), 0); // 3 products -> 0 promotion

    }

    @Test
    public void scanProduct_moreThanOneProductType_allWithPromotion_shouldCalculateValidSum() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode_1 = "A";
        String productCode_2 = "B";

        Product product_1 = dao.getByProductCode(productCode_1);
        Product product_2 = dao.getByProductCode(productCode_2);

        double price_1 = product_1.getPrice();
        double price_2 = product_2.getPrice();
        double promoPrice_1 = product_1.getPromotion().getPrice();
        double promoPrice_2 = product_2.getPromotion().getPrice();

        shopCash.scanProduct(productCode_1);
        shopCash.scanProduct(productCode_2);
        // P_1: 1 products -> 0 promotion, P_2: 1 products -> 0 promotion
        Assert.assertEquals(price_1 + price_2, shopCash.checkBill(), 0);

        shopCash.scanProduct(productCode_2);
        // P_1: 1 products -> 0 promotion, P_2: 2 products -> 1 promotion
        Assert.assertEquals(price_1 + promoPrice_2, shopCash.checkBill(), 0);

        shopCash.scanProduct(productCode_1);
        shopCash.scanProduct(productCode_1);
        // P_1:: 3 products -> 1 promotion, P_2:: 2 products -> 1 promotion
        Assert.assertEquals(promoPrice_1 + promoPrice_2, shopCash.checkBill(), 0);
    }

    @Test
    public void scanProduct_moreThanOneProductType_someWithPromotion_shouldCalculateValidSum() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode_1 = "A";
        String productCode_2 = "C";

        Product product_1 = dao.getByProductCode(productCode_1);
        Product product_2 = dao.getByProductCode(productCode_2);

        double price_1 = product_1.getPrice();
        double price_2 = product_2.getPrice();
        double promoPrice_1 = product_1.getPromotion().getPrice();

        shopCash.scanProduct(productCode_1);
        shopCash.scanProduct(productCode_2);
        // P_1: 1 products -> 0 promotion, P_2: 1 products -> 0 promotion
        Assert.assertEquals(price_1 + price_2, shopCash.checkBill(), 0);

        shopCash.scanProduct(productCode_1);
        shopCash.scanProduct(productCode_2);
        // P_1: 2 products -> 0 promotion, P_2: 2 products -> 0 promotion
        Assert.assertEquals(price_1 + price_1 + price_2 + price_2, shopCash.checkBill(), 0);

        shopCash.scanProduct(productCode_1);
        shopCash.scanProduct(productCode_2);
        // P_1: 3 products -> 1 promotion, P_2: 3 products -> 0 promotion
        Assert.assertEquals(promoPrice_1 + price_2 + price_2 + price_2, shopCash.checkBill(), 0);
    }

    @Test
    public void scanProduct_productCodeDoesNotExists_shouldCalculateValidSum() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode = "A";
        String fakeProductCode = "FAKE";
        Product product = dao.getByProductCode(productCode);

        double price = product.getPrice();

        shopCash.scanProduct(productCode);
        Assert.assertEquals(price, shopCash.checkBill(), 0); // 1 product -> 0 promotion

        shopCash.scanProduct(fakeProductCode);
        Assert.assertEquals(price, shopCash.checkBill(), 0); // 1 product -> 0 promotion
    }

    @Test
    public void scanProduct_productCodeIsNull_shouldCalculateValidSum() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode = "A";
        Product product = dao.getByProductCode(productCode);

        double price = product.getPrice();

        shopCash.scanProduct(productCode);
        Assert.assertEquals(price, shopCash.checkBill(), 0); // 1 product -> 0 promotion

        shopCash.scanProduct(null);
        Assert.assertEquals(price, shopCash.checkBill(), 0); // 1 product -> 0 promotion
    }

    @Test
    public void checkBill_noProductScanned_shouldShowZero() {
        ShopCash shopCash = new SafeShopCash(dao);
        Assert.assertEquals(0, shopCash.checkBill(), 0); // 1 product -> 0 promotion
    }

    @Test
    public void printBill_productsAdded_streamIsNull_shouldClearState() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode_1 = "A";
        String productCode_2 = "D";

        Product product_1 = dao.getByProductCode(productCode_1);
        Product product_2 = dao.getByProductCode(productCode_2);

        double price_1 = product_1.getPrice();
        double price_2 = product_2.getPrice();

        shopCash.scanProduct(productCode_1);
        shopCash.scanProduct(productCode_2);
        // P_1: 1 products -> 0 promotion, P_2: 1 products -> 0 promotion
        Assert.assertEquals(price_1 + price_2, shopCash.checkBill(), 0);

        shopCash.printBill(null);
        Assert.assertEquals(0, shopCash.checkBill(), 0);

        shopCash.scanProduct(productCode_1);
        shopCash.scanProduct(productCode_2);
        // P_1: 1 products -> 0 promotion, P_2: 1 products -> 0 promotion
        Assert.assertEquals(price_1 + price_2, shopCash.checkBill(), 0);
    }

    @Test
    public void printBill_noProducts_streamIsNull_shouldClearState() {
        ShopCash shopCash = new SafeShopCash(dao);

        Assert.assertEquals(0, shopCash.checkBill(), 0);

        shopCash.printBill(null);
        Assert.assertEquals(0, shopCash.checkBill(), 0);
    }

    @Test
    public void printBill_streamIsNotNull_shouldPrintSumIntoStream() {
        ShopCash shopCash = new SafeShopCash(dao);

        String productCode = "A";
        Product product = dao.getByProductCode(productCode);

        double price = product.getPrice();


        shopCash.scanProduct(productCode);
        Assert.assertEquals(price, shopCash.checkBill(), 0);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()))) {
            shopCash.printBill(out);
            Assert.assertEquals(price, in.readDouble(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
