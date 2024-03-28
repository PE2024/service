package ro.unibuc.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.contact.data.MessageEntity;
import ro.unibuc.contact.data.MessageRepository;
import ro.unibuc.contact.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;


@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public MessageEntity createMessage(MessageEntity message) {
        try {
            return messageRepository.save(message);
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteMessage(String messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new EntityNotFoundException("Message not found with ID: " + messageId);
        }
        try {
            messageRepository.deleteById(messageId);
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<MessageEntity> findById(String messageId) {
        return messageRepository.findById(messageId);
    }

    public List<MessageEntity> getMessagesForUser(String userId) {
        return messageRepository.findByUserId(userId);
    }

}

