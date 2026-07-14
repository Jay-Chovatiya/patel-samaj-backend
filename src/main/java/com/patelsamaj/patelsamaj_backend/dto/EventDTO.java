package com.patelsamaj.patelsamaj_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDTO {
    private Integer eventId;

    @NotBlank(message = "Family head name is required")
    private String familyHeadName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be exactly 10 digits")
    private String contactNumber;

    @Email(message = "Must be a valid email format")
    private String email;

    @NotBlank(message = "Event type is required (e.g., Wedding, Meeting)")
    private String eventType;

    @NotNull(message = "Event date cannot be null")
    private LocalDateTime eventDate;

    @PositiveOrZero(message = "Total amount cannot be negative")
    private Long totalAmount;

    private Boolean isClosed;
    private Integer statusId;

    @Valid
    private List<ItemSelectionDTO> items;
}