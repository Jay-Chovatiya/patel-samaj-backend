package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.dto.ChangePasswordDTO;
import com.patelsamaj.patelsamaj_backend.dto.ForgotPasswordDTO;
import com.patelsamaj.patelsamaj_backend.dto.UserProfileDTO;

public interface UserService {
    UserProfileDTO updateProfile(int userId, UserProfileDTO dto);
    void resetAndEmailPassword(ForgotPasswordDTO email);
    UserProfileDTO getUserProfile(int adminId);
    void changePassword(int userId, ChangePasswordDTO dto);
}
