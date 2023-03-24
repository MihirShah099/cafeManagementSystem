package com.inn.cafe.serviceImpl;

import com.inn.cafe.JWT.CustomerUserDetailsService;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.JWT.JwtUtil;
import com.inn.cafe.POJO.User;
import com.inn.cafe.constents.cafeConstant;
import com.inn.cafe.dao.userDao;
import com.inn.cafe.service.userService;
import com.inn.cafe.utils.EmailUtils;
import com.inn.cafe.utils.cafeUtils;
import com.inn.cafe.wrapper.userWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class userServiceimpl implements userService {
    @Autowired
    userDao userDao;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMapped) {
        log.info("Inside signUp {}",requestMapped);
        try {
            if (validateSignUpMap(requestMapped)) {
                User user = userDao.findByEmailId(requestMapped.get("email"));
                if (Objects.isNull(user)) {

                    userDao.save(getUserFromMap(requestMapped));
                    return cafeUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return cafeUtils.getResponseEntity("email Already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return cafeUtils.getResponseEntity(cafeConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cafeUtils.getResponseEntity(cafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMapped) {
       log.info("Inside login");
       try{
           Authentication auth=authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(requestMapped.get("email"),requestMapped.get("password"))
           );
            if(auth.isAuthenticated()){
                if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateToken(customerUserDetailsService.
                                    getUserDetail().getEmail(), customerUserDetailsService.getUserDetail().getRole())+"\"}",
                            HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message\":\""+"wait for Admin approval."+"\"}",
                            HttpStatus.BAD_REQUEST);
                }

            }
       }catch(Exception ex){
           log.error("{}",ex);
       }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",
                HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<userWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }

        }catch(Exception ex){
            ex.printStackTrace();

        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
              Optional<User> optional =userDao.findById(Integer.parseInt(requestMap.get("id")));
              if(!optional.isEmpty()){
                    userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                    return cafeUtils.getResponseEntity("User Status updated Successfully",HttpStatus.OK);
              }else {
                  cafeUtils.getResponseEntity("User id doesn't exist",HttpStatus.OK);
              }


            }else{
                return cafeUtils.getResponseEntity(cafeConstant.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cafeUtils.getResponseEntity(cafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
            allAdmin.remove(jwtFilter.getCurrentUser());
            if(status!=null && status.equalsIgnoreCase("true")){
                        emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
                                "Account is Approved",
                                "USer:- "+user+"\n isApproved by \n ADMIN:-"+jwtFilter.getCurrentUser()
                                ,allAdmin);
            }else {
                emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
                        "Account is Disabled",
                        "USer:- "+user+"\n isDisabled by \n ADMIN:-"+jwtFilter.getCurrentUser()
                        ,allAdmin);
            }
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
      if(requestMap.containsKey("name")&& requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password"))
      {
          return true;
      }
      return false;
    }
    private User getUserFromMap(Map<String,String> requestMap){
        User user=new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;

    }

}
