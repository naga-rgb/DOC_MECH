package com.example.doc_mech;

public class Add_shop {
    String shopname,uid,status,lastseen,shop_status;
    String url;

    public Add_shop(String shopname, String uid,String status,String lastseen,String shop_status) {
        this.shopname = shopname;
        this.uid = uid;
        this.status = status;
        this.lastseen = lastseen;
        this.shop_status = shop_status;
    }

    public Add_shop(String url) {
        this.url = url;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getShop_status() {
        return shop_status;
    }

    public void setShop_status(String shop_status) {
        this.shop_status = shop_status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}



