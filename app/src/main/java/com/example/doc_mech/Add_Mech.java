package com.example.doc_mech;

public class Add_Mech {

    private String  Shop_name, Owner_name, Location, Email, Phone,Password,uid;
    private int mech_coins;

    public Add_Mech(String shop_name, String owner_name, String location, String email, String phone, String password, int mech_coins,String uid) {
        Shop_name = shop_name;
        Owner_name = owner_name;
        Location = location;
        Email = email;
        Phone = phone;
        Password = password;
        this.mech_coins = mech_coins;
        this.uid = uid;
    }

    public Add_Mech() {
    }

    public String getShop_name() {
        return Shop_name;
    }

    public void setShop_name(String shop_name) {
        Shop_name = shop_name;
    }

    public String getOwner_name() {
        return Owner_name;
    }

    public void setOwner_name(String owner_name) {
        Owner_name = owner_name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getMech_coins() {
        return mech_coins;
    }

    public void setMech_coins(int mech_coins) {
        this.mech_coins = mech_coins;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
