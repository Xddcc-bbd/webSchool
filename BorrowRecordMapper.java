package com.example.mystudent.mapper;

import com.example.mystudent.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BorrowRecordMapper {
    @Select("select * from borrow_record")
    List<BorrowRecord> getAllWithDetails();
    @Update("UPDATE borrow_record SET backDay = #{backDay} " +
            "WHERE isbn = #{isbn} AND userID = #{userID} AND backDay IS NULL")
    int updateReturnDate(@Param("isbn") String isbn,
                         @Param("userID") int userID,
                         @Param("backDay") String backDay);
}