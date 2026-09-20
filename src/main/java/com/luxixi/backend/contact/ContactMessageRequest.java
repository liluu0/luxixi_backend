package com.luxixi.backend.contact;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record ContactMessageRequest(@NotBlank @Size(max=80) String visitorName, @NotBlank @Size(max=2000) String message) {}
