package com.macht.foodowl.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class OrderAdapter implements Parcelable {
   public String orderid;
   public String user_id;
   public String time;
   public String order_state;
   public String order_status;
   public int amount_paid;
   public int item_total;
   public int delivery_amount;
   public int discount;
   public DeliveryDetail deliveryDetail;
   public CartDetails orderdetail;
   public String total_order_string;
   public Map<String,CartElement> cart_list;


    public  OrderAdapter(){}

    public OrderAdapter(String orderid, String user_id, String time, String order_state, String order_status, int amount_paid,
                        int item_total, int delivery_amount, int discount, DeliveryDetail deliveryDetail, CartDetails orderdetail,
                        String total_order_string, Map<String, CartElement> cart_list) {
        this.orderid = orderid;
        this.user_id = user_id;
        this.time = time;
        this.order_state = order_state;
        this.order_status = order_status;
        this.amount_paid = amount_paid;
        this.item_total = item_total;
        this.delivery_amount = delivery_amount;
        this.discount = discount;
        this.deliveryDetail = deliveryDetail;
        this.orderdetail = orderdetail;
        this.total_order_string = total_order_string;
        this.cart_list = cart_list;
    }

    public OrderAdapter(String orderid, String user_id, String time, String order_state, String order_status, int amount_paid,
                        int item_total, int delivery_amount, int discount, DeliveryDetail deliveryDetail, CartDetails orderdetail,
                        Map<String, CartElement> cart_list) {
        this.orderid = orderid;
        this.user_id = user_id;
        this.time = time;
        this.order_state = order_state;
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
        user_id = in.readString();
        time = in.readString();
        order_state = in.readString();
        order_status = in.readString();
        amount_paid = in.readInt();
        item_total = in.readInt();
        delivery_amount = in.readInt();
        discount = in.readInt();
        deliveryDetail = in.readParcelable(DeliveryDetail.class.getClassLoader());
        total_order_string = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderid);
        dest.writeString(user_id);
        dest.writeString(time);
        dest.writeString(order_state);
        dest.writeString(order_status);
        dest.writeInt(amount_paid);
        dest.writeInt(item_total);
        dest.writeInt(delivery_amount);
        dest.writeInt(discount);
        dest.writeParcelable(deliveryDetail, flags);
        dest.writeString(total_order_string);
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
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

    public String getTotal_order_string() {
        return total_order_string;
    }

    public void setTotal_order_string(String total_order_string) {
        this.total_order_string = total_order_string;
    }

    public Map<String, CartElement> getCart_list() {
        return cart_list;
    }

    public void setCart_list(Map<String, CartElement> cart_list) {
        this.cart_list = cart_list;
    }

    public static Creator<OrderAdapter> getCREATOR() {
        return CREATOR;
    }
}
