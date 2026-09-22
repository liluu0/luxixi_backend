package com.luxixi.backend.contact;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ContactMessageResponse(
        Long id,
        String visitorName,
        String message,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime submittedAt) {
    static ContactMessageResponse from(ContactMessage m){return new ContactMessageResponse(m.getId(),m.getVisitorName(),m.getMessage(),m.getSubmittedAt());}
}
