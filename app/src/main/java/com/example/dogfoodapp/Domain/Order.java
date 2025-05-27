package com.example.dogfoodapp.Domain;
import java.util.ArrayList;


public class Order {
    private String userEmail,Orders;
    private double total;
    private ArrayList<ItemsDomain>items;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order( String userEmail, double total, ArrayList<ItemsDomain>items) {
        this.userEmail = userEmail;
        this.total = total;
        this.items = items;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<ItemsDomain> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    public String getOrder(){
        return Orders;
    }

    public void setOrder(String Order){
        this.Orders = Order;
    }
}