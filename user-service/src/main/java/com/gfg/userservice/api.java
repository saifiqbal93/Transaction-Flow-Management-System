package com.gfg.userservice;

import com.gfg.userservice.entity.SignUp;
import com.gfg.userservice.entity.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class api {

    @Autowired
    UserManager userManager;

    @PostMapping("/user")
    ResponseEntity createUser(@RequestBody SignUp signUp){
        userManager.createUser(signUp);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/user/{username}")
    ResponseEntity getUser(@PathVariable("username") String username){
        try {
            UserResponse userResponse = userManager.getUser(username);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
