package com.patelsamaj.patelsamaj_backend.repository;

import com.patelsamaj.patelsamaj_backend.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findByStatusId(Integer statusId, Pageable pageable);

    @Query("""
        SELECT e 
        FROM Event e
        WHERE e.statusId = :statusId
        AND (
            :search IS NULL 
            OR :search = ''
            OR LOWER(e.familyHeadName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(e.eventType) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        """)
    Page<Event> findByStatusIdAndSearch(
            @Param("statusId") Integer statusId,
            @Param("search") String search,
            Pageable pageable
    );
}
