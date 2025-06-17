package com.example.mystudent.controller;

import com.example.mystudent.entity.Book;
import com.example.mystudent.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class BookController {
    @Autowired
    BookMapper bookMapper;
    //query all books' information
    @RequestMapping("/get-all")
    public List<Book> getall(){
        return bookMapper.getall();
    }
    //delete one book
    @RequestMapping("/delbook")
    public int delBook(String isbn){
        return bookMapper.DelBook(isbn);
    }
    //add book's information
    @RequestMapping("/addbook")
    public int addBook(@RequestBody Book book){
        return bookMapper.addBook(book);
    }
    //modify book's information
    @RequestMapping("/upbook")
    public int upBook(@RequestBody Book book){
        return bookMapper.upBook(book);
    }
    @RequestMapping("/search-books")
    public List<Book> searchBooks(String isbn, String bookName, String writer, String pubDay) {
        return bookMapper.searchBooks(isbn, bookName, writer, pubDay);
    }
}
