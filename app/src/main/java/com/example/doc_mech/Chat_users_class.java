package com.example.doc_mech;

public class Chat_users_class {
    private String username,userid,status,lastseen;

    public Chat_users_class(String username, String userid,String status,String lastseen) {
        this.username = username;
        this.userid = userid;
        this.status = status;
        this.lastseen = lastseen;

    }

    public Chat_users_class() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }
}
