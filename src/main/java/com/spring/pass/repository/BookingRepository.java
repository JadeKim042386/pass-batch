package com.spring.pass.repository;

import com.spring.pass.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE Booking b" +
            "          SET b.usedPass = :usedPass," +
            "              b.modifiedAt = CURRENT_TIMESTAMP" +
            "        WHERE b.passTicket.id = :passTicketId")
    int updateUsedPass(@Param("passTicketId") Long passTicketId, @Param("usedPass") boolean usedPass);
}
