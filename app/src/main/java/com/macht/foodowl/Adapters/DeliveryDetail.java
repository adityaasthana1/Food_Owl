package com.macht.foodowl.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryDetail implements Parcelable {
    String addressid,fullname, mobilenumber,pincode,housenumber,area,landmark,city,state,fulladdress;

    public DeliveryDetail(){}

    public DeliveryDetail(String addressid, String fullname, String mobilenumber, String pincode, String housenumber, String area, String landmark, String city, String state, String fulladdress) {
        this.addressid = addressid;
        this.fullname = fullname;
        this.mobilenumber = mobilenumber;
        this.pincode = pincode;
        this.housenumber = housenumber;
        this.area = area;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.fulladdress = fullname + ", " + housenumber + ", " + area + ", " + city;
    }

    protected DeliveryDetail(Parcel in) {
        addressid = in.readString();
        fullname = in.readString();
        mobilenumber = in.readString();
        pincode = in.readString();
        housenumber = in.readString();
        area = in.readString();
        landmark = in.readString();
        city = in.readString();
        state = in.readString();
    }

    public static final Creator<DeliveryDetail> CREATOR = new Creator<DeliveryDetail>() {
        @Override
        public DeliveryDetail createFromParcel(Parcel in) {
            return new DeliveryDetail(in);
        }

        @Override
        public DeliveryDetail[] newArray(int size) {
            return new DeliveryDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressid);
        dest.writeString(fullname);
        dest.writeString(mobilenumber);
        dest.writeString(pincode);
        dest.writeString(housenumber);
        dest.writeString(area);
        dest.writeString(landmark);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(fulladdress);
    }

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public static Creator<DeliveryDetail> getCREATOR() {
        return CREATOR;
    }
}
