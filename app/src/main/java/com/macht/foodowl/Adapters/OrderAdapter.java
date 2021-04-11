package com.macht.foodowl.Adapters;

public class OrderAdapter {
    String OrderName, OrderId, OrderCost;

    OrderAdapter(){}

    public OrderAdapter(String orderName, String orderId, String orderCost) {
        OrderName = orderName;
        OrderId = orderId;
        OrderCost = orderCost;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderCost() {
        return OrderCost;
    }

    public void setOrderCost(String orderCost) {
        OrderCost = orderCost;
    }
}
