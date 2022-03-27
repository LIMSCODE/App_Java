package com.example.myapplication1.model;

public class User {
    public String userEmail;
    public String userPassword;
    public String userPasswordCheck;
    public String userName;
    public String userPhone;

    public User(String email, String pwd, String pwdcheck, String name, String phone) {
        email = this.userEmail;
        pwd = this.userPassword;
        pwdcheck = this.userPasswordCheck;
        name = this.userName;
        phone = this.userPhone;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPasswordCheck() {
        return userPasswordCheck;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }



    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPasswordCheck(String userPasswordCheck) {
        this.userPasswordCheck = userPasswordCheck;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }






}
