package com.inn.cafe.restImpl;

import com.inn.cafe.constents.cafeConstant;
import com.inn.cafe.rest.userRest;
import com.inn.cafe.service.userService;
import com.inn.cafe.utils.cafeUtils;
import com.inn.cafe.wrapper.userWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class userRestImpl implements userRest {
    @Autowired
    userService userService;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMapped) {
        try{
            return userService.signUp(requestMapped);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cafeUtils.getResponseEntity(cafeConstant.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return userService.login(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return cafeUtils.getResponseEntity(cafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<userWrapper>> getAllUser() {
        try{
            return userService.getAllUser();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<userWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            return userService.update(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cafeUtils.getResponseEntity(cafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
