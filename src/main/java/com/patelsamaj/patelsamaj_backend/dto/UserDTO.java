package com.patelsamaj.patelsamaj_backend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;

    private String fullName;

    private String mobileNumber;

    private Boolean isActive;
}
