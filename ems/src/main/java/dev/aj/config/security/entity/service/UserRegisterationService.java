package dev.aj.config.security.entity.service;

import dev.aj.config.security.dto.LoginUserDetails;

public interface UserRegisterationService {
    LoginUserDetails registerUser(LoginUserDetails loginUserDetails);
}
