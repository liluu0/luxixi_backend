package com.luxixi.backend.contact;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
@Service
public class ContactMessageService {
    private final ContactMessageRepository repository;
    public ContactMessageService(ContactMessageRepository repository){this.repository=repository;}
    @Transactional public ContactMessageResponse create(ContactMessageRequest request){
        String visitorName = ContactTextPolicy.normalizeName(request.visitorName());
        String message = ContactTextPolicy.normalizeMessage(request.message());
        if(repository.existsByVisitorNameAndMessageAndSubmittedAtAfter(visitorName, message, OffsetDateTime.now().minusMinutes(5))) throw new DuplicateMessageException();
        ContactMessage entity=new ContactMessage(); entity.setVisitorName(visitorName); entity.setMessage(message);
        return ContactMessageResponse.from(repository.save(entity));
    }
    public static class DuplicateMessageException extends RuntimeException {}
}
