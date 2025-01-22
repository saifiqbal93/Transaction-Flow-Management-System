package com.gfg.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    UserClient userClient;

    @KafkaListener(topics = "txnotification", groupId = "notification")
    void receiveTxNotification(String txNotification){
        log.info(txNotification);
        String firstChar =  txNotification.substring(0,1);
        String username = txNotification.substring(1,txNotification.length());

        UserResponse userResponse = userClient.getUser(username);

        if(firstChar.equals("1")){
            log.info("tx for user "+ userResponse.getEmail() + " is success");
        }else{
            log.error("tx for user "+ userResponse.getEmail() + " has failed");
        }

    }

    @KafkaListener(topics = "user", groupId = "notification")
    void receiveUserNotification(String user){
        log.info(user + " has successfully registered.");
    }
}
