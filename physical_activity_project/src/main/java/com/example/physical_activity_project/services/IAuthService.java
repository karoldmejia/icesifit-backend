package com.example.demo.service;

import com.example.demo.dto.LoginRequest;

public interface IAuthService {
    public String login(LoginRequest request);
}
