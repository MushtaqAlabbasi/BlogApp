package com.mushtaq.blogapp.Activities;

import org.json.JSONException;
import org.json.JSONObject;

public  class CommentsModel {


    private String comText, uID,pID,uname,comTime;


    public CommentsModel() {
    }

    public CommentsModel(String comText, String uID, String uname, String comTime) {
        this.comText = comText;
        this.uID = uID;
        this.uname = uname;
        this.comTime = comTime;

    }



    public CommentsModel(String comText, String uID, String pId) {
        this.comText = comText;
        this.uID = uID;
        this.pID = pId;
    }


    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getComText() {
        return comText;
    }

    public void setComText(String comText) {
        this.comText = comText;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getComTime() {
        return comTime;
    }

    public void setComTime(String comTime) {
        this.comTime = comTime;
    }


    static CommentsModel createComment(JSONObject JOcomment) throws JSONException {

        CommentsModel commentsModel =new CommentsModel();
        commentsModel.setUname(JOcomment.getString("u_name"));
        commentsModel.setComText(JOcomment.getString("com_text"));
        commentsModel.setComTime(JOcomment.getString("com_time"));
        //        commentsModel.set(JOcomment.getString("p_category"));
        //        commentsModel.setTimeStamp(JOcomment.getString("p_time"));
        return commentsModel;

    }


}
