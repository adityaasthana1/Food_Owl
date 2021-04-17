package com.macht.foodowl.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable{
    public String foodid;
    public String foodname;
    public String foodtype;
    public String foodtaste;
    public String fooddescription;
    public String foodgenre;
    public String foodcategory;
    public String foodprice;
    public String foodavailability;
    public String foodtag;
    public String imageURL;

    public FoodItem(){
        this.foodavailability = "na";
        this.foodcategory = "na";
        this.foodid = "na";
        this.foodname = "na";
        this.foodtype = "na";
        this.foodtaste = "na";
        this.fooddescription = " na";
        this.foodgenre = "na";
        this.foodtag = "na";
        this.foodtype = "na";
    }

    public FoodItem(String foodid, String foodname, String foodtype, String foodtaste, String fooddescription, String foodgenre, String foodcategory, String foodprice, String foodavailability, String foodtag,String imageURL) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.foodtype = foodtype;
        this.foodtaste = foodtaste;
        this.fooddescription = fooddescription;
        this.foodgenre = foodgenre;
        this.foodcategory = foodcategory;
        this.foodprice = foodprice;
        this.foodavailability = foodavailability;
        this.foodtag = foodtag;
        this.imageURL = imageURL;
    }

    protected FoodItem(Parcel in) {
        foodid = in.readString();
        foodname = in.readString();
        foodtype = in.readString();
        foodtaste = in.readString();
        fooddescription = in.readString();
        foodgenre = in.readString();
        foodcategory = in.readString();
        foodprice = in.readString();
        foodavailability = in.readString();
        foodtag = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

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

    public String getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(String foodtype) {
        this.foodtype = foodtype;
    }

    public String getFoodtaste() {
        return foodtaste;
    }

    public void setFoodtaste(String foodtaste) {
        this.foodtaste = foodtaste;
    }

    public String getFooddescription() {
        return fooddescription;
    }

    public void setFooddescription(String fooddescription) {
        this.fooddescription = fooddescription;
    }

    public String getFoodgenre() {
        return foodgenre;
    }

    public void setFoodgenre(String foodgenre) {
        this.foodgenre = foodgenre;
    }

    public String getFoodcategory() {
        return foodcategory;
    }

    public void setFoodcategory(String foodcategory) {
        this.foodcategory = foodcategory;
    }

    public String getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(String foodprice) {
        this.foodprice = foodprice;
    }

    public String getFoodavailability() {
        return foodavailability;
    }

    public void setFoodavailability(String foodavailability) {
        this.foodavailability = foodavailability;
    }

    public String getFoodtag() {
        return foodtag;
    }

    public void setFoodtag(String foodtag) {
        this.foodtag = foodtag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodid);
        dest.writeString(foodname);
        dest.writeString(foodtype);
        dest.writeString(foodtaste);
        dest.writeString(fooddescription);
        dest.writeString(foodgenre);
        dest.writeString(foodcategory);
        dest.writeString(foodprice);
        dest.writeString(foodavailability);
        dest.writeString(foodtag);
    }
}
