package com.example.mystudent.entity;

public class Book {
    String isbn;
    String bookName;
    String writer;
    String pubDay;

    public String getisbn() {
        return isbn;
    }

    public void setisbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPubDay() {
        return pubDay;
    }

    public void setPubDay(String pubDay) {
        this.pubDay = pubDay;
    }
}
