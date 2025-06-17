package com.example.mystudent.mapper;

import com.example.mystudent.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {
    //query all books' information
    @Select("select * from book")
    List<Book> getall();
    //delete one book
    @Delete("delete from book where isbn=#{isbn}")
    int DelBook(String isbn);
    //add book's information
    @Insert("insert into book values(#{isbn},#{bookName},#{writer},#{pubDay})")
    int addBook(Book book);
    //modify book's information
    @Update("update book set bookName=#{bookName},writer=#{writer},pubDay=#{pubDay} where isbn=#{isbn}")
    int upBook(Book book);
    @Select("<script>" +
            "SELECT * FROM book WHERE 1=1" +
            "<if test='isbn != null and isbn != \"\"'> AND ISBN LIKE CONCAT('%',#{isbn},'%')</if>" +
            "<if test='bookName != null and bookName != \"\"'> AND bookName LIKE CONCAT('%',#{bookName},'%')</if>" +
            "<if test='writer != null and writer != \"\"'> AND writer LIKE CONCAT('%',#{writer},'%')</if>" +
            "<if test='pubDay != null and pubDay != \"\"'> AND pubDay LIKE CONCAT('%',#{pubDay},'%')</if>" +
            "</script>")
    List<Book> searchBooks(@Param("isbn") String isbn,
                           @Param("bookName") String bookName,
                           @Param("writer") String writer,
                           @Param("pubDay") String pubDay);
}
