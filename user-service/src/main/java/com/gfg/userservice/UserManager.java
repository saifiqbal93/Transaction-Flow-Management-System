package com.gfg.userservice;

import com.gfg.userservice.entity.SignUp;
import com.gfg.userservice.entity.UserResponse;

public interface UserManager {
    void createUser(SignUp signUp);
    UserResponse getUser(String username) throws Exception;
}
