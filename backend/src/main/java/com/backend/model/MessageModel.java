package com.backend.model;

public class MessageModel {
    private int id;
    private String user_id;
    private String content;
    private String status;
    private String priority;

    public MessageModel (){};
    public MessageModel(int id,String user_id,String content,String status){
        this.id=id;
        this.user_id=user_id;
        this.content=content;
        this.status=status;
    }
    public MessageModel(int id,String user_id,String content,String status,String priority){
        this.id=id;
        this.user_id=user_id;
        this.content=content;
        this.status=status;
        this.priority=priority;
    }
    public MessageModel(int id,String status){
        this.id=id;
        this.status=status;
    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getUserId(){return user_id;}
    public void setUserId(String user_id){this.user_id=user_id;}

    public String getContent(){return content;}
    public void setContent(String content){this.content=content;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public String getPriority(){return priority;}
    public void setPriority(String priority){this.priority=priority;}
    
}
