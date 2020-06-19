package com.example.doc_mech;

public class addjobcardclass {
    String orderid,bike_no,owner,kms,email,phone,engine,suspension,electrical,others,general_services,premium_services,date,Address,User,shopname;


    public addjobcardclass(String orderid,String bike_no, String owner, String kms, String Address, String email, String phone, String general_services, String premium_services, String engine, String suspension, String electrical, String others, String date, String User, String shopname) {
        this.orderid = orderid;
        this.bike_no = bike_no;
        this.owner = owner;
        this.kms = kms;
        this.email = email;
        this.phone = phone;
        this.engine = engine;
        this.suspension = suspension;
        this.electrical = electrical;
        this.others = others;
        this.general_services = general_services;
        this.premium_services = premium_services;
        this.date = date;
        this.Address = Address;
        this.User = User;
        this.shopname = shopname;
    }

    public addjobcardclass()
    {
    }

    public String getGeneral_services() {
        return general_services;
    }

    public void setGeneral_services(String general_services) {
        this.general_services = general_services;
    }

    public String getPremium_services() {
        return premium_services;
    }

    public void setPremium_services(String premium_services) {
        this.premium_services = premium_services;
    }


    public String getBike_no() {
        return bike_no;
    }

    public void setBike_no(String bike_no) {
        this.bike_no = bike_no;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getSuspension() {
        return suspension;
    }

    public void setSuspension(String suspension) {
        this.suspension = suspension;
    }

    public String getElectrical() {
        return electrical;
    }

    public void setElectrical(String electrical) {
        this.electrical = electrical;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
