package com.example.amazon.Model;

public class AdminOrders {

    private  String name , totalPrice , state , data , time, address , city  , phone ;

    public AdminOrders() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice=totalPrice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state=state;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data=data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time=time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address=address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city=city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public AdminOrders(String name, String totalPrice, String state, String data, String time, String address, String city, String phone) {
        this.name=name;
        this.totalPrice=totalPrice;
        this.state=state;
        this.data=data;
        this.time=time;
        this.address=address;
        this.city=city;
        this.phone=phone;
    }
}
