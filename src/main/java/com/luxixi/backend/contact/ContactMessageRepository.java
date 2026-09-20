package com.luxixi.backend.contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.OffsetDateTime;
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    boolean existsByVisitorNameAndMessageAndSubmittedAtAfter(String visitorName, String message, OffsetDateTime after);
}
