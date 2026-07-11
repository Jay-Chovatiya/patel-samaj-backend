package com.patelsamaj.patelsamaj_backend.controller;

import com.patelsamaj.patelsamaj_backend.dto.DonorDTO;
import com.patelsamaj.patelsamaj_backend.helper.SecurityUtils;
import com.patelsamaj.patelsamaj_backend.service.DonorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonorController {
    private final DonorService donorService;

    @PostMapping
    public ResponseEntity<DonorDTO> upsertDonor(@Valid @RequestBody DonorDTO donorDTO) {
        int adminId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(donorService.upsertDonor(donorDTO, adminId));
    }

    @GetMapping("{donorId}")
    public ResponseEntity<DonorDTO> getDonorById(@PathVariable int donorId){
        return ResponseEntity.ok(donorService.getDonorById(donorId));
    }

    @GetMapping
    public ResponseEntity<List<DonorDTO>> getAllDonors(){
        return ResponseEntity.ok(donorService.getDonorList());
    }
}
