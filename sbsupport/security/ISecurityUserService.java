package com.springboot.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
