package com.project.grocery.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.grocery.Entity.Bill;
import com.project.grocery.Service.BillService;

import java.util.Map;
@RestController
public class BillController {

    @Autowired
    private BillService billService;


    @Autowired

        private static final Logger logger = LoggerFactory.getLogger(BillController.class);
        @PostMapping("/Bill")
        public ResponseEntity<Map<String, Object>> generateBill(@RequestBody Bill billRequest) {
            // Log the incoming request
            logger.info("Received request to generate bill: {}", billRequest);
    
            Map<String, Object> response = billService.generateBill(billRequest);
    
            // Log the response
            logger.info("Generated bill response: {}", response);
    
            return ResponseEntity.ok(response);
        }
    }