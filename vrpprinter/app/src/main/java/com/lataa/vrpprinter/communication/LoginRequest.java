package com.lataa.vrpprinter.communication;

public class LoginRequest {
    public String login;
    public String password;
    public LoginRequest(String login,String password){
        this.login=login;
        this.password=password;
    }
}
