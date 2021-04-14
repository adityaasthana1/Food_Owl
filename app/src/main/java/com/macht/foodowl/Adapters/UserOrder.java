package com.macht.foodowl.Adapters;

import java.util.Map;

public class UserOrder {
    String order_id;
    String order_status;
    int grand_total;
    Map time;

    public UserOrder(){}

    public UserOrder(String order_id, String order_status, int grand_total, Map time) {
        this.order_id = order_id;
        this.order_status = order_status;
        this.grand_total = grand_total;
        this.time = time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public int getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(int grand_total) {
        this.grand_total = grand_total;
    }

    public Map getTime() {
        return time;
    }

    public void setTime(Map time) {
        this.time = time;
    }
}
