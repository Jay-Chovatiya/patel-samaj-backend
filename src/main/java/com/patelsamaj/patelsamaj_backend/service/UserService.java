package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.dto.ChangePasswordDTO;
import com.patelsamaj.patelsamaj_backend.dto.ForgotPasswordDTO;
import com.patelsamaj.patelsamaj_backend.dto.UserDTO;
import com.patelsamaj.patelsamaj_backend.dto.UserProfileDTO;

import java.util.List;

public interface UserService {
    UserProfileDTO updateProfile(int userId, UserProfileDTO dto);
    String inviteNewUser(UserProfileDTO dto, int userId);
    List<UserDTO> getAllUsers();
    Boolean toggleAdminActivation(int userId);
    void resetAndEmailPassword(ForgotPasswordDTO email);
    UserProfileDTO getUserProfile(int adminId);
    void changePassword(int userId, ChangePasswordDTO dto);
}
