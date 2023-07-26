package com.spring.pass.repository;

import com.spring.pass.domain.BulkPassTicket;
import com.spring.pass.domain.constant.BulkPassTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkPassTicketRepository extends JpaRepository<BulkPassTicket, Long> {
    /**
     * WHERE status = :status AND startedAt > :startedAt
     * @param status: {@link BulkPassTicketStatus}
     * @param startedAt: {@link LocalDateTime}
     */
    List<BulkPassTicket> findByStatusAndStartedAtGreaterThan(BulkPassTicketStatus status, LocalDateTime startedAt);
}
