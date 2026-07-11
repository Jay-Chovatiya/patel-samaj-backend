package com.patelsamaj.patelsamaj_backend.mapper;

import com.patelsamaj.patelsamaj_backend.dto.UserProfileDTO;
import com.patelsamaj.patelsamaj_backend.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserProfileDTO, User> {
}
