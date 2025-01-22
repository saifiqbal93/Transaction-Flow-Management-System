package com.gfg.userservice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUp {
    private String username;
    private String email;
}
