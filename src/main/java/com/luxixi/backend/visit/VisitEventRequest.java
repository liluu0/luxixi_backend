package com.luxixi.backend.visit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public record VisitEventRequest(@NotBlank @Pattern(regexp = "/") String path) {}
