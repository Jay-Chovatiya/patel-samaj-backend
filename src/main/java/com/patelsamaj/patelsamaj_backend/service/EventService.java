package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.dto.EventDTO;

import java.util.List;

public interface EventService {

    EventDTO getEventById(int eventId);

    List<EventDTO> getAllEvent();

    List<EventDTO> getEventsByStatusId(int statusId);

    EventDTO upsertEvent(EventDTO eventDTO, int userId);

    int closeBill(int eventId);

}
