package dev.aj.config.security.service;

import dev.aj.config.security.dto.UserRegisterationDetails;

public interface UserRegisterationService {
    UserRegisterationDetails registerUser(UserRegisterationDetails userRegisterationDetails);
}
