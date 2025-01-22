package com.gfg.userservice;

import com.gfg.userservice.entity.SignUp;
import com.gfg.userservice.entity.User;
import com.gfg.userservice.entity.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserManagerImpl implements UserManager{
    @Autowired
    UserRepository userRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void createUser(SignUp signUp) {
        User user = User.builder()
                .email(signUp.getEmail())
                .username(signUp.getUsername())
                .build();
        userRepository.save(user);
        kafkaTemplate.send("user", user.getUsername());
    }

    @Override
    public UserResponse getUser(String username) throws Exception {
       User user = userRepository.findByUsername(username)
               .orElseThrow(()-> new Exception("User not found"));
       return UserResponse.builder()
               .email(user.getEmail())
               .username(user.getUsername())
               .build();
    }
}
