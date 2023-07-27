package com.spring.pass.repository;

import com.spring.pass.domain.PassTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PassTicketRepository extends JpaRepository<PassTicket, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE PassTicket p" +
            "          SET p.remainingCount = :remainingCount," +
            "              p.modifiedAt = CURRENT_TIMESTAMP" +
            "        WHERE p.id = :passTicketId")
    int updateRemainingCount(@Param("passTicketId") Long passTicketId, @Param("remainingCount") Integer remainingCount);
}
