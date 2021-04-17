package com.macht.foodowl.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDataAdapter implements Parcelable {
    public String fname,lname,email,phone;
    public String uri;

    public UserDataAdapter(String fname, String lname, String email, String phone, String uri) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.uri = uri;
    }

    public UserDataAdapter(){
        this.lname = " ";
        this.fname = " ";
        this.email = " ";
        this.phone = " ";
        this.uri = "";
    }

    public UserDataAdapter(String fname, String lname, String email, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
    }

    protected UserDataAdapter(Parcel in) {
        fname = in.readString();
        lname = in.readString();
        email = in.readString();
        phone = in.readString();
        uri = in.readString();
    }

    public static final Creator<UserDataAdapter> CREATOR = new Creator<UserDataAdapter>() {
        @Override
        public UserDataAdapter createFromParcel(Parcel in) {
            return new UserDataAdapter(in);
        }

        @Override
        public UserDataAdapter[] newArray(int size) {
            return new UserDataAdapter[size];
        }
    };

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(uri);
    }
}
