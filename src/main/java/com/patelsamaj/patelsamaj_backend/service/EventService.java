package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.dto.EventDTO;
import com.patelsamaj.patelsamaj_backend.model.Event;

import java.util.List;

public interface EventService {

    EventDTO getEventById(int eventId);

    List<EventDTO> getAllEvent();

    List<EventDTO> getInProgressEvents();

    EventDTO upsertEvent(EventDTO eventDTO, int userId);

    int closeBill(int eventId);

}
