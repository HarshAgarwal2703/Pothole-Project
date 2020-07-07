package com.example.potholedetectiondemo.model.Request;

import okhttp3.RequestBody;

public class PostRequestModel {



    private RequestBody title;
    private RequestBody Description;
    private RequestBody app_user_field;
    private RequestBody latitude;
    private RequestBody longitude;
    private RequestBody created_date;
    private RequestBody published_date;
    private RequestBody status;
    private RequestBody vote_count;
    private RequestBody address;



    public RequestBody getTitle() {
        return title;
    }

    public void setTitle(RequestBody title) {
        this.title = title;
    }

    public RequestBody getDescription() {
        return Description;
    }

    public void setDescription(RequestBody description) {
        Description = description;
    }

    public RequestBody getApp_user_field() {
        return app_user_field;
    }

    public void setApp_user_field(RequestBody app_user_field) {
        this.app_user_field = app_user_field;
    }

    public RequestBody getLatitude() {
        return latitude;
    }

    public void setLatitude(RequestBody latitude) {
        this.latitude = latitude;
    }

    public RequestBody getLongitude() {
        return longitude;
    }

    public void setLongitude(RequestBody longitude) {
        this.longitude = longitude;
    }

    public RequestBody getCreated_date() {
        return created_date;
    }

    public void setCreated_date(RequestBody created_date) {
        this.created_date = created_date;
    }

    public RequestBody getPublished_date() {
        return published_date;
    }

    public void setPublished_date(RequestBody published_date) {
        this.published_date = published_date;
    }

    public RequestBody getStatus() {
        return status;
    }

    public void setStatus(RequestBody status) {
        this.status = status;
    }

    public RequestBody getVote_count() {
        return vote_count;
    }

    public void setVote_count(RequestBody vote_count) {
        this.vote_count = vote_count;
    }

    public RequestBody getAddress() {
        return address;
    }

    public void setAddress(RequestBody address) {
        this.address = address;
    }
}
