package com.oltranz.auth.controller;

import com.oltranz.auth.model.Token;
import com.oltranz.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oltranz.auth.Utils.JwtTokenUtil;
import com.oltranz.auth.repository.UserRepository;

import java.util.List;

@Controller
@RequestMapping
public class UserController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user){
        String hashPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        ;
        if(((User) userRepository.save(user)).getId()>0)
        return ResponseEntity.ok("User was saved");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user was not save, Internal server error");
    }

    @PostMapping("/generate-token")
    public ResponseEntity<Object> generateToken(@RequestBody Token tokenReqRes){
        User databaseUser = userRepository.findByUsername(tokenReqRes.getUsername());
        if (databaseUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry, User Does Not Exist");
        }
        if (new BCryptPasswordEncoder().matches(tokenReqRes.getPassword(), databaseUser.getPassword())){
            String token = jwtTokenUtil.generateToken(tokenReqRes.getUsername());
            tokenReqRes.setToken(token);
            tokenReqRes.setExpirationTime("5 min");
            return ResponseEntity.ok(tokenReqRes);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password Doesn't Match. Verify");
        }
    }

        @PostMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody Token tokenReqRes){
        return ResponseEntity.ok(jwtTokenUtil.validateToken(tokenReqRes.getToken()));
    }
    
     @GetMapping("/get-fruits")
    public  ResponseEntity<Object> getAllFruits(@RequestHeader(value = "Authorization", required = false) String token){
        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        }else{
            String realToken = token.substring(7);
            String tokenCheckResult = jwtTokenUtil.validateToken(realToken);
            if (tokenCheckResult.equalsIgnoreCase("valid")){
                List<String> fruits = List.of("Mango", "Banana", "Orange", "Watermellon", "Grapes", "Appple",
                        "Berries");
                return new ResponseEntity<>(fruits, HttpStatus.OK);
            }else{
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unautorixed dur to: " + tokenCheckResult);
            }
        }
    }

    
}
