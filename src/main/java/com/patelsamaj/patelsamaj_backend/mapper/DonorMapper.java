package com.patelsamaj.patelsamaj_backend.mapper;

import com.patelsamaj.patelsamaj_backend.dto.DonorDTO;
import com.patelsamaj.patelsamaj_backend.model.Donor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DonorMapper extends BaseMapper<DonorDTO, Donor> {

    @Override
    @Mapping(target = "donationDate", ignore = true)
    void updateEntityFromDto(DonorDTO dto, @MappingTarget Donor entity);
}
