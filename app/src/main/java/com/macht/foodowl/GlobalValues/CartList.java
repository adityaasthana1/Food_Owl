package com.macht.foodowl.GlobalValues;

import android.app.Application;

import com.macht.foodowl.Adapters.CartElement;

import java.util.ArrayList;
public class CartList extends Application {
    public ArrayList<CartElement> cartElements;
    public CartList(){
        this.cartElements = new ArrayList<>();
    }

    public void addtoCart(CartElement cartElement){
        this.cartElements.add(cartElement);
    }

}
