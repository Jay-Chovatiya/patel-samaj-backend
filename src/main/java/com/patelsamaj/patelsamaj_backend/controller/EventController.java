package com.patelsamaj.patelsamaj_backend.controller;

import com.patelsamaj.patelsamaj_backend.dto.EventDTO;
import com.patelsamaj.patelsamaj_backend.dto.PaginationDTO;
import com.patelsamaj.patelsamaj_backend.helper.SecurityUtils;
import com.patelsamaj.patelsamaj_backend.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/list")
    public ResponseEntity<Page<EventDTO>> getEvents(
            @RequestParam Integer statusId,
            @RequestBody PaginationDTO request) {

        request.setSortBy("createdAt");

        return ResponseEntity.ok(
                eventService.getEventsByStatusId(statusId, request)
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable int eventId){
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        int adminId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(eventService.upsertEvent(eventDTO, adminId));
    }

    @PutMapping
    public ResponseEntity<EventDTO> updateEvent(@Valid @RequestBody EventDTO eventDTO) {
        int adminId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(eventService.upsertEvent(eventDTO, adminId));
    }

    @PutMapping("/close/{eventId}")
    public ResponseEntity<Integer> closeBill(@PathVariable Integer eventId) {
        return ResponseEntity.ok(eventService.closeBill(eventId));
    }
}
