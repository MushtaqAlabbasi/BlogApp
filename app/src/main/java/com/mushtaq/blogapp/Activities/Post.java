package com.mushtaq.blogapp.Activities;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Post implements Serializable {


    private String userId;
    private String pID;

    private String content;

    private String userName;
    private String userPhoto;
    private String timeStamp ;
    private String category;
    private String currentUser;

    public Post( String category,String content,String userId,String currentUser) {
        this.content = content;
        this.userId = userId;
        this.category = category;
        this.currentUser = currentUser;
    }

    public Post( String category,String content,String currentUser) {
        this.content = content;
        this.currentUser = currentUser;
        this.category = category;
    }
    // make sure to have an empty constructor inside ur model class
    public Post() {
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {

        if (userPhoto != null)
            return ServerApp.IP + "blog/image/" + userPhoto;
        else
            return null;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    static Post createPost(JSONObject JOpost) throws JSONException {

        Post post=new Post();
//        post.setUserPhoto(JOpost.getString("u_photo"));
//        post.setUserName(JOpost.getString("u_name"));
//        post.setContent(JOpost.getString("p_content"));
//        post.setCategory(JOpost.getString("p_category"));
//        post.setTimeStamp(JOpost.getString("p_time"));

        post.setpID(JOpost.getString("p_id"));
        post.setUserId(JOpost.getString("u_id"));
        post.setUserName(JOpost.getString("u_name"));
        post.setContent(JOpost.getString("p_content"));
        post.setCategory(JOpost.getString("cat_name"));
        post.setTimeStamp(JOpost.getString("p_time"));
        post.setUserPhoto(JOpost.getString("u_photo"));
        post.setCurrentUser(CurrentUserSession.getUID());

        return post;

    }

}
