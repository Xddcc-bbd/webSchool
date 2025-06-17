package com.example.mystudent.controller;

import com.example.mystudent.entity.BorrowRecord;
import com.example.mystudent.mapper.BorrowRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrow-records")
@CrossOrigin
public class BorrowRecordController {

    @Autowired
    BorrowRecordMapper borrowRecordMapper;

    @GetMapping
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordMapper.getAllWithDetails();
    }
    @PutMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody Map<String, Object> request) {
        try {
            String isbn = (String) request.get("isbn");
            int userID = (Integer) request.get("userID");
            String backDay = (String) request.get("backDay");

            int result = borrowRecordMapper.updateReturnDate(isbn, userID, backDay);

            if (result > 0) {
                return ResponseEntity.ok().body(Map.of("success", true, "message", "归还成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "归还失败，记录不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}