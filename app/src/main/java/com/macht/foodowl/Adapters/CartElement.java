package com.macht.foodowl.Adapters;

public class CartElement {
    public String foodid;
    public String foodname;
    public String price;
    public int quantity;
    public int value;

    public CartElement(){
        foodid = "";
        foodname = "";
        price = "";
        quantity = 0;
        value = 0;
    }

    public CartElement( String foodid, String foodname, String price, int quantity) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.price = price;
        this.quantity = quantity;
        this.value = Integer.parseInt(price)*quantity;
    }

    public CartElement(String foodid, String foodname, String price, int quantity, int value) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.price = price;
        this.quantity = quantity;
        this.value = value;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
