package com.macht.foodowl.models;

public class DeleteUser {
    public String userid, useremail, time, delete_status;

    public DeleteUser(){}

    public DeleteUser(String userid, String useremail, String time, String delete_status) {
        this.userid = userid;
        this.useremail = useremail;
        this.time = time;
        this.delete_status = delete_status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }
}
