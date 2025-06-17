package com.example.mystudent.entity;

public class BorrowRecord {
    private String isbn;
    private int userID;
    private String outDay;
    private String backDay;
    private String bookName; // 用于关联查询
    private String userName; // 用于关联查询

    // getters and setters


    public String getisbn() {
        return isbn;
    }

    public void setisbn(String isbn) {
        this.isbn = isbn;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getOutDay() {
        return outDay;
    }

    public void setOutDay(String outDay) {
        this.outDay = outDay;
    }

    public String getBackDay() {
        return backDay;
    }

    public void setBackDay(String backDay) {
        this.backDay = backDay;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}