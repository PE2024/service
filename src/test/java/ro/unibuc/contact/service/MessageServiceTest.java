package ro.unibuc.contact.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.unibuc.contact.data.InformationEntity;
import ro.unibuc.contact.data.MessageEntity;
import ro.unibuc.contact.data.MessageRepository;
import ro.unibuc.contact.data.UserEntity;
import ro.unibuc.contact.data.UserRepository;
import ro.unibuc.contact.dto.Greeting;
import ro.unibuc.contact.dto.MessageDTO;
import ro.unibuc.contact.dto.UserDTO;
import ro.unibuc.contact.exception.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import java.util.Optional;

import javax.xml.bind.annotation.W3CDomHandler;

@ExtendWith(SpringExtension.class)
class MessageServiceTest{

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageService messageService= new MessageService();

    private MessageDTO messageDTO;
    private MessageEntity messageEntity;


    @BeforeEach
    void setUp() {
        messageDTO = new MessageDTO("username", "salut", "salut");
        messageEntity = new MessageEntity("salut", "salut", "1");
    }

    @Test
    void testCreateMessage() {
        messageService.createMessage(messageEntity);

        Assertions.assertEquals("salut", messageEntity.subject);
        Assertions.assertEquals("salut", messageEntity.body);
        Assertions.assertEquals("1", messageEntity.userId);
    }

    @Test
    void testDeleteMessage() {
        String messageId = "1";
        when(messageRepository.existsById(messageId)).thenReturn(true);

        messageService.deleteMessage(messageId);

        verify(messageRepository, times(1)).deleteById(messageId);
    }

    @Test
    void testDeleteMessage_throwsEntityNotFoundException() {
        String messageId = "1";
        when(messageRepository.existsById(messageId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(messageId));
    }

    @Test 
    void testFindById() {
        String messageId = "1";
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(messageEntity));

        Optional<MessageEntity> message = messageService.findById(messageId);

        assertEquals(messageEntity, message.get());
    }

    @Test
    void testGetMessagesForUser() {
        String userId = "1";
        when(messageRepository.findByUserId(userId)).thenReturn(List.of(messageEntity));

        List<MessageEntity> messages = messageService.getMessagesForUser(userId);

        assertEquals(1, messages.size());
        assertEquals(messageEntity, messages.get(0));
    }

}