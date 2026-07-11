package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.dto.DonorDTO;

import java.util.List;

public interface DonorService {
    DonorDTO upsertDonor(DonorDTO dto, int userId);
    List<DonorDTO> getDonorList();
    DonorDTO getDonorById(int donorId);

}
