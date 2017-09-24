package com.ubs;

import com.ubs.dao.ProductDAO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ShopCash {
    protected ProductDAO dao;

    public ShopCash(ProductDAO dao) {
        if (dao == null) {
            throw new IllegalArgumentException("dao parameter cannot be null");
        }
        this.dao = dao;
    }

    public void printBill(OutputStream stream) {
        if (stream != null) {
            try {
                new DataOutputStream(stream).writeDouble(checkBill());
            } catch (IOException e) {
                System.out.println(checkBill());
            }
        }
        closeBill();
    }

    public abstract double checkBill();
    public abstract void scanProduct(String productCode);

    protected abstract void closeBill();
}
