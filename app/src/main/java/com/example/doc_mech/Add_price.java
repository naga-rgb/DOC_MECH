package com.example.doc_mech;

public class Add_price {

    String general,premium;

    public Add_price(String general, String premium) {
        this.general = general;
        this.premium = premium;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }
}
