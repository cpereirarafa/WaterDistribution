package com.university.finalwork.database;

public class Products {

    private String productId;
    private String productName;
    private String productDescription;
    private String productSize;
    private double productPrice;
    private long productStock;

    public Products(){}

    public Products(String productId, String productName, String productDescription, String productSize, long productPrice, long productStock) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    public Products(String productId, String productName, String productDescription, String productSize, double productPrice, long productStock) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public long getProductStock() {
        return productStock;
    }

    public void setProductStock(long productStock) {
        this.productStock = productStock;
    }
}
