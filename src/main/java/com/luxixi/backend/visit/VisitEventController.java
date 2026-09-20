package com.luxixi.backend.visit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visit-events")
public class VisitEventController {
    private final VisitEventService service;
    public VisitEventController(VisitEventService service){this.service=service;}
    @PostMapping @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@Valid @RequestBody VisitEventRequest request, HttpServletRequest httpRequest){service.create(request, httpRequest);}
}
