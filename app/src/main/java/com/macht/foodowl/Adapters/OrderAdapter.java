package com.macht.foodowl.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class OrderAdapter implements Parcelable {
   public String orderid;
   public String time;
   public String order_status;
   public int amount_paid;
   public int item_total;
   public int delivery_amount;
   public int discount;
   public DeliveryDetail deliveryDetail;
   public CartDetails orderdetail;
   public Map<String,CartElement> cart_list;


   public  OrderAdapter(){}

    public OrderAdapter(String orderid, String time, String order_status, int amount_paid, int item_total, int delivery_amount, int discount, DeliveryDetail deliveryDetail, CartDetails orderdetail, Map<String, CartElement> cart_list) {
        this.orderid = orderid;
        this.time = time;
        this.order_status = order_status;
        this.amount_paid = amount_paid;
        this.item_total = item_total;
        this.delivery_amount = delivery_amount;
        this.discount = discount;
        this.deliveryDetail = deliveryDetail;
        this.orderdetail = orderdetail;
        this.cart_list = cart_list;
    }

    protected OrderAdapter(Parcel in) {
        orderid = in.readString();
        time = in.readString();
        order_status = in.readString();
        amount_paid = in.readInt();
        item_total = in.readInt();
        delivery_amount = in.readInt();
        discount = in.readInt();
        deliveryDetail = in.readParcelable(DeliveryDetail.class.getClassLoader());
    }

    public static final Creator<OrderAdapter> CREATOR = new Creator<OrderAdapter>() {
        @Override
        public OrderAdapter createFromParcel(Parcel in) {
            return new OrderAdapter(in);
        }

        @Override
        public OrderAdapter[] newArray(int size) {
            return new OrderAdapter[size];
        }
    };

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public int getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(int amount_paid) {
        this.amount_paid = amount_paid;
    }

    public int getItem_total() {
        return item_total;
    }

    public void setItem_total(int item_total) {
        this.item_total = item_total;
    }

    public int getDelivery_amount() {
        return delivery_amount;
    }

    public void setDelivery_amount(int delivery_amount) {
        this.delivery_amount = delivery_amount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

    public CartDetails getOrderdetail() {
        return orderdetail;
    }

    public void setOrderdetail(CartDetails orderdetail) {
        this.orderdetail = orderdetail;
    }

    public Map<String, CartElement> getCart_list() {
        return cart_list;
    }

    public void setCart_list(Map<String, CartElement> cart_list) {
        this.cart_list = cart_list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderid);
        dest.writeString(time);
        dest.writeString(order_status);
        dest.writeInt(amount_paid);
        dest.writeInt(item_total);
        dest.writeInt(delivery_amount);
        dest.writeInt(discount);
        dest.writeParcelable(deliveryDetail, flags);
    }
}
