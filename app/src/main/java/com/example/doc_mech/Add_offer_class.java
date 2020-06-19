package com.example.doc_mech;

public class Add_offer_class {

    String offer_name, offer_description,shop_name;

    public Add_offer_class(String offer_name, String offer_description,String shop_name) {
        this.offer_name = offer_name;
        this.offer_description = offer_description;
        this.shop_name = shop_name;
    }

    public Add_offer_class() {
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
    }

    public String getShop_name() {
        return shop_name;
    }
    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}