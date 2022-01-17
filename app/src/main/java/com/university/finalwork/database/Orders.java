package com.university.finalwork.database;

import java.util.List;
import java.util.Map;

public class Orders {

    private String orderId;
    private String userId;
    private String productId;
    private long productQuantity;
    private boolean wasDelivered;

    public Orders(String orderId, String userId, String productId, long productQuantity, boolean wasDelivered) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.wasDelivered = wasDelivered;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public boolean isWasDelivered() {
        return wasDelivered;
    }

    public void setWasDelivered(boolean wasDelivered) {
        this.wasDelivered = wasDelivered;
    }
}
