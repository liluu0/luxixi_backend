package com.luxixi.backend.contact;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "contact_message")
public class ContactMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "visitor_name", nullable = false, length = 80)
    private String visitorName;
    @Column(nullable = false, length = 2000)
    private String message;
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private OffsetDateTime submittedAt;
    @PrePersist void onCreate() { submittedAt = OffsetDateTime.now(); }
    public Long getId(){return id;} public String getVisitorName(){return visitorName;} public void setVisitorName(String v){visitorName=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;} public OffsetDateTime getSubmittedAt(){return submittedAt;}
}
