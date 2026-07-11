package com.patelsamaj.patelsamaj_backend.service.impl;

import com.patelsamaj.patelsamaj_backend.dto.EventDTO;
import com.patelsamaj.patelsamaj_backend.dto.ItemSelectionDTO;
import com.patelsamaj.patelsamaj_backend.exception.ResourceNotFoundException;
import com.patelsamaj.patelsamaj_backend.mapper.EventMapper;
import com.patelsamaj.patelsamaj_backend.model.*;
import com.patelsamaj.patelsamaj_backend.repository.*;
import com.patelsamaj.patelsamaj_backend.service.EmailService;
import com.patelsamaj.patelsamaj_backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ItemRepository itemRepository;
    private final EventMapper eventMapper;
    private final EmailService emailService;

    @Override
    public EventDTO getEventById(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        return eventMapper.toDto(event);
    }

    @Override
    public List<EventDTO> getAllEvent(){
        return eventMapper.toDtoList(eventRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public List<EventDTO> getInProgressEvents(){
        return eventMapper.toDtoList(eventRepository.findByStatusId(3));
    }

    @Override
    @Transactional
    public EventDTO upsertEvent(EventDTO dto, int userId) {
        Event event;
        if(dto.getEventId() != null && dto.getEventId() != 0){
            event = eventRepository.findById(dto.getEventId()).orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
            if (Boolean.TRUE.equals(event.getIsClosed())) {
                throw new IllegalStateException("Bill is finalized and locked.");
            }

            if (event.getBookedItems() != null) {
                event.getBookedItems().clear();
            }
        }
        else{
            event = new Event();
            event.setIsClosed(false);
        }

        eventMapper.updateEventFromDto(dto, event);

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (ItemSelectionDTO selection : dto.getItems()) {
            Item masterItem = itemRepository.findById(selection.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item " + selection.getItemId() + " not found"));

            EventItem snapshotItem = new EventItem();
            snapshotItem.setEvent(event);
            snapshotItem.setItem(masterItem);
            snapshotItem.setQuantity(selection.getQuantity());
            snapshotItem.setPriceAtBooking(masterItem.getCurrentPrice());

            BigDecimal lineTotal = snapshotItem.getPriceAtBooking()
                    .multiply(BigDecimal.valueOf(selection.getQuantity()));
            grandTotal = grandTotal.add(lineTotal);

            event.getBookedItems().add(snapshotItem);
        }

        event.setTotalAmount(grandTotal);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public int closeBill(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        if (Boolean.TRUE.equals(event.getIsClosed())) {
            throw new IllegalStateException("This bill is locked and cannot be edited.");
        }

        event.setIsClosed(true);
        event.setStatusId(3); // Set to 'Closed'
        eventRepository.save(event);
        emailService.sendBillReceipt(event);
        return event.getEventId();
    }
}