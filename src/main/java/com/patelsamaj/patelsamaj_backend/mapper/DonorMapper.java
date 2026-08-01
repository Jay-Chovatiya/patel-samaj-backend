package com.patelsamaj.patelsamaj_backend.mapper;

import com.patelsamaj.patelsamaj_backend.dto.DonorDTO;
import com.patelsamaj.patelsamaj_backend.model.Donor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DonorMapper extends BaseMapper<DonorDTO, Donor> {
}
