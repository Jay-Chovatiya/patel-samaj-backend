package com.patelsamaj.patelsamaj_backend.repository;

import com.patelsamaj.patelsamaj_backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByStatusId(Integer statusId);
}
