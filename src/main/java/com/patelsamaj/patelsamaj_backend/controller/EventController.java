package com.patelsamaj.patelsamaj_backend.controller;

import com.patelsamaj.patelsamaj_backend.dto.EventDTO;
import com.patelsamaj.patelsamaj_backend.helper.SecurityUtils;
import com.patelsamaj.patelsamaj_backend.model.Event;
import com.patelsamaj.patelsamaj_backend.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(){
        return ResponseEntity.ok(eventService.getAllEvent());
    }

    @GetMapping("/in-progress")
    public ResponseEntity<List<EventDTO>> getAllInProgressEvent(){
        return ResponseEntity.ok(eventService.getInProgressEvents());
    }

    @GetMapping("{eventId}")
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
