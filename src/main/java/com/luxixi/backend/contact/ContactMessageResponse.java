package com.luxixi.backend.contact;
import java.time.OffsetDateTime;
public record ContactMessageResponse(Long id, String visitorName, String message, OffsetDateTime submittedAt) {
    static ContactMessageResponse from(ContactMessage m){return new ContactMessageResponse(m.getId(),m.getVisitorName(),m.getMessage(),m.getSubmittedAt());}
}
