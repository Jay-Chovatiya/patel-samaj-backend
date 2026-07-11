package com.patelsamaj.patelsamaj_backend.controller;

import com.patelsamaj.patelsamaj_backend.dto.ChangePasswordDTO;
import com.patelsamaj.patelsamaj_backend.dto.UserProfileDTO;
import com.patelsamaj.patelsamaj_backend.helper.SecurityUtils;
import com.patelsamaj.patelsamaj_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile(){
        int userId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateMyProfile(@Valid @RequestBody UserProfileDTO dto) {
        int userId = SecurityUtils.getAuthenticatedUserId();
        userService.updateProfile(userId, dto);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO dto){
        int userId = SecurityUtils.getAuthenticatedUserId();
        userService.changePassword(userId, dto);
        return ResponseEntity.ok("Password changed successfully");
    }
}
