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

public class RegisterRequestModel {

    private String first_name;
    private String last_name;
    private String phone_number;
    private String email_id;
    private String password;


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }





}
