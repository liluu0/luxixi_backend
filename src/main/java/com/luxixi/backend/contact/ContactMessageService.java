package com.luxixi.backend.contact;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
@Service
public class ContactMessageService {
    private final ContactMessageRepository repository;
    public ContactMessageService(ContactMessageRepository repository){this.repository=repository;}
    @Transactional public ContactMessageResponse create(ContactMessageRequest request){
        if(repository.existsByVisitorNameAndMessageAndSubmittedAtAfter(request.visitorName(), request.message(), OffsetDateTime.now().minusMinutes(5))) throw new DuplicateMessageException();
        ContactMessage entity=new ContactMessage(); entity.setVisitorName(request.visitorName().trim()); entity.setMessage(request.message().trim());
        return ContactMessageResponse.from(repository.save(entity));
    }
    public static class DuplicateMessageException extends RuntimeException {}
}
