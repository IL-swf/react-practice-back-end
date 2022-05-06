package com.il.reactpracticebackend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    public static ResponseEntity<String> response () {


        return ResponseEntity.ok("okay");
    }

    public static ResponseEntity<String> notFound () {


        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public static ResponseEntity<String> badRequest () {


        return ResponseEntity.badRequest().body("this failed");
    }

}
