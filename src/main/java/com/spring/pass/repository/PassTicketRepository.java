package com.spring.pass.repository;

import com.spring.pass.domain.PassTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassTicketRepository extends JpaRepository<PassTicket, Long> {
}
