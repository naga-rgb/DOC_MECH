package com.example.doc_mech;

public class Image {
    String url,UID;

    public Image(String url,String UID) {
        this.url = url;
        this.UID = UID;
    }

    public Image() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
