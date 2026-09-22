package com.luxixi.backend.contact;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    boolean existsByVisitorNameAndMessageAndSubmittedAtAfter(String visitorName, String message, LocalDateTime after);
}
