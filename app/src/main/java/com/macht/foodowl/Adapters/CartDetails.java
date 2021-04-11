package com.macht.foodowl.Adapters;

public class CartDetails {
    public int totalamount;
    public String date;

    public CartDetails(){
        this.totalamount = 0;
        this.date = "";
    }

    public CartDetails(int totalamount, String date) {
        this.totalamount = totalamount;
        this.date = date;
    }

    public int getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(int totalamount) {
        this.totalamount = totalamount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
