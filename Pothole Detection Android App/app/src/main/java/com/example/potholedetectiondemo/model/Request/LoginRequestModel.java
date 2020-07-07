package com.example.potholedetectiondemo.model.Request;
/*

{
        "id": 5,
        "first_name": "user4",
        "last_name": "lastname4",
        "email_id": "email4@gmail.com",
        "password": "hello",
        "phone_number": 4077916064
    }

 */
public class LoginRequestModel {

    private String email_id;
    private String password;




    public void setEmailID(String emailID) {
        this.email_id = emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailID() {
        return email_id;
    }
}
