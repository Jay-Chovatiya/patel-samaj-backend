package com.patelsamaj.patelsamaj_backend.service.impl;

import com.patelsamaj.patelsamaj_backend.dto.DonorDTO;
import com.patelsamaj.patelsamaj_backend.exception.ResourceNotFoundException;
import com.patelsamaj.patelsamaj_backend.mapper.DonorMapper;
import com.patelsamaj.patelsamaj_backend.model.Donor;
import com.patelsamaj.patelsamaj_backend.repository.DonorRepository;
import com.patelsamaj.patelsamaj_backend.service.DonorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorServiceImpl implements DonorService  {

    private final DonorRepository donorRepository;
    private final DonorMapper donorMapper;

    @Override
    @Transactional
    public DonorDTO upsertDonor(DonorDTO dto, int userId) {
        Donor donor;
        if (dto.getDonorId() != null && dto.getDonorId() != 0) {
            donor = donorRepository.findById(dto.getDonorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + dto.getDonorId()));
        } else {
            donor = new Donor();
            donor.setCreatedBy(userId);
            donor.setDonationDate(LocalDateTime.now());
        }
        donorMapper.updateEntityFromDto(dto, donor);
        Donor savedDonor = donorRepository.save(donor);
        return donorMapper.toDto(savedDonor);
    }

    @Override
    public List<DonorDTO> getDonorList() {
        return donorMapper.toDtoList(donorRepository.findAll(Sort.by(Sort.Direction.DESC, "amount")));
    }

    @Override
    public DonorDTO getDonorById(int donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found"));
        return donorMapper.toDto(donor);
    }
}
