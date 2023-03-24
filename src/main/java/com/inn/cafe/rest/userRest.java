package com.inn.cafe.rest;

import com.inn.cafe.wrapper.userWrapper;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;
import java.util.Map;

@RequestMapping(path="/user")
public interface userRest {
    @PostMapping(path="/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMapped);

    @PostMapping(path="/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);
    @GetMapping(path="/get")
    public ResponseEntity<List<userWrapper>> getAllUser();
    @PostMapping(path="/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);
}
