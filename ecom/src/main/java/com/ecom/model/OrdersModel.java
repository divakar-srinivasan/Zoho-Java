package com.ecom.model;

public class OrdersModel {
    private int orderId;
    private String userId;
    private String productName;
    private String type;
    private int price;
    private String status;
    private String priority;

    public OrdersModel(){}

    public OrdersModel(int orderId,String userId,String productName,String type,int price,String status,String priority){
        this.orderId=orderId;
        this.userId=userId;
        this.productName=productName;
        this.type=type;
        this.price=price;
        this.status=status;
        this.priority=priority;
    }
    public int getOrderId(){return orderId;}
    public void setOrderId(int orderId){this.orderId=orderId;}

    public String getUserID(){return userId;}
    public void estUserId(String userId){this.userId=userId;}

    public String getProductName(){return productName;}
    public void setProductName(String productName){this.productName=productName;}

    public String getType(){return type;}
    public void setType(String type){this.type=type;}

    public int getPrice(){return price;}
    public void setPrice(int price){this.price=price;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public String getPriority(){return priority;}
    public void setPriority(String priority){this.priority=priority;}


}
