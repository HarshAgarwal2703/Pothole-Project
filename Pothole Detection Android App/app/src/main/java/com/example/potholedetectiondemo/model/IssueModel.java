package com.example.potholedetectiondemo.model;


import com.google.gson.annotations.SerializedName;

public class IssueModel {


    @SerializedName("id")
    private int id;

    @SerializedName("latitude")
    private String Latitude;
    @SerializedName("longitude")
    private String Longitude;
    @SerializedName("vote_count")
    private int votes;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("published_date")
    private String published_Date;
    private boolean checkLiked;
    @SerializedName("title")
    private String title;
    @SerializedName("Description")
    private String description;
    @SerializedName("status")
    private String status;

    public IssueModel(int id, String latitude, String longitude, int votes, String published_Date, boolean checkLiked, String title, String description, String status) {
        this.id = id;
        Latitude = latitude;
        Longitude = longitude;
        this.votes = votes;
        this.published_Date = published_Date;
        this.checkLiked = checkLiked;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueModel() {
        super();
    }

    public boolean isCheckLiked() {
        return checkLiked;
    }

    public void setCheckLiked(boolean checkLiked) {
        this.checkLiked = checkLiked;
    }

    public IssueModel(int id, String Latitude, String Longitude, int votes, String imageUrl, String published_Date) {
        this.id = id;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.votes = votes;
        this.imageUrl = imageUrl;
        this.published_Date = published_Date;
    }

    public String getPublished_Date() {
        return published_Date;
    }

    public void setPublished_Date(String published_Date) {
        this.published_Date = published_Date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "IssueModel{" +
                "id=" + id +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", votes=" + votes +
                ", imageUrl='" + imageUrl + '\'' +
                ", published_Date='" + published_Date + '\'' +
                ", checkLiked=" + checkLiked +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
