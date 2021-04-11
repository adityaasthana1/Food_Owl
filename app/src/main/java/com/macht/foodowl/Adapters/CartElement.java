package com.macht.foodowl.Adapters;

public class CartElement {
    public String id;
    public String foodid;
    public String foodname;
    public String price;
    public int quantity;

    public CartElement(String id, String foodid, String foodname, String price, int quantity) {
        this.id = id;
        this.foodid = foodid;
        this.foodname = foodname;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
