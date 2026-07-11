package com.patelsamaj.patelsamaj_backend.service.impl;

import com.patelsamaj.patelsamaj_backend.dto.ChangePasswordDTO;
import com.patelsamaj.patelsamaj_backend.dto.ForgotPasswordDTO;
import com.patelsamaj.patelsamaj_backend.dto.UserProfileDTO;
import com.patelsamaj.patelsamaj_backend.exception.ResourceNotFoundException;
import com.patelsamaj.patelsamaj_backend.mapper.UserMapper;
import com.patelsamaj.patelsamaj_backend.model.User;
import com.patelsamaj.patelsamaj_backend.repository.UserRepository;
import com.patelsamaj.patelsamaj_backend.service.EmailService;
import com.patelsamaj.patelsamaj_backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserProfileDTO getUserProfile(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(int userId, UserProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getMobileNumber() != null) user.setMobileNumber(dto.getMobileNumber());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        userRepository.save(user);
        return dto;
    }

    @Override
    public void changePassword(int userId, ChangePasswordDTO dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password. Cannot change password.");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetAndEmailPassword(ForgotPasswordDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email"));

        String randomPlainPassword = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        String hashedPassword = passwordEncoder.encode(randomPlainPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        emailService.sendNewPasswordEmail(user.getEmail(), randomPlainPassword, user.getFullName());
    }
}
