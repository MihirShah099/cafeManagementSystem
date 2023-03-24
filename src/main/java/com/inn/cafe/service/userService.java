package com.inn.cafe.service;

import com.inn.cafe.wrapper.userWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface userService {
    ResponseEntity<String> signUp(Map<String, String> requestMapped);
    ResponseEntity<String> login(Map<String, String> requestMapped);

    ResponseEntity<List<userWrapper>> getAllUser();

    ResponseEntity<String> update(Map<String, String> requestMap);
}
