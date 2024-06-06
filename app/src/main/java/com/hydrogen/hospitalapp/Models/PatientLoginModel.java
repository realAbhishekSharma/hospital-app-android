package com.hydrogen.hospitalapp.Models;

public class PatientLoginModel {

    private String UserName;
    private String Password;
    private String Token;

    public PatientLoginModel(String userName, String password) {
        this.UserName = userName;
        this.Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }




}
