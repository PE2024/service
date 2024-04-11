package ro.unibuc.contact.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ro.unibuc.contact.data.MessageEntity;
import ro.unibuc.contact.data.UserEntity;
import ro.unibuc.contact.dto.Greeting;
import ro.unibuc.contact.dto.MessageDTO;
import ro.unibuc.contact.dto.UserAuthDTO;
import ro.unibuc.contact.dto.UserDTO;
import ro.unibuc.contact.exception.EntityNotFoundException;
import ro.unibuc.contact.service.MessageService;
import ro.unibuc.contact.service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.mock.mockito.MockBean;



import java.util.Optional;

public class MessageControllerTest {
    @Mock
    private MessageService messageService;

     @Mock
    private UserService userService;
    
    @InjectMocks
    private MessageController messageController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); 
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
        objectMapper = new ObjectMapper();
    }

    private UserDTO exampleUserDTO = new UserDTO("user1", "user1@example.com", "password");
    private MessageDTO exampleMessageDTO = new MessageDTO("Hello", "This is a test message", "user1");


    private UserEntity exampleUserEntity = new UserEntity("user1", "user1@example.com", "password");
    private UserAuthDTO exampleUserAuthDTO = new UserAuthDTO("user1", "password");

    @Test
    void createMessage_UserNotFound_ShouldReturnBadRequest() throws Exception {
    when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

    mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exampleMessageDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("User not found"));
    }

    @Test
    void createMessage_ValidRequest_ShouldReturnOk() throws Exception {
    when(userService.findByUsername(exampleMessageDTO.getUsername())).thenReturn(Optional.of(exampleUserEntity));
    when(messageService.createMessage(any(MessageEntity.class))).thenReturn(new MessageEntity());

    mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exampleMessageDTO)))
            .andExpect(status().isOk());
}

    @Test
    void getMessage_UserNotFound_ShouldReturnBadRequest() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exampleUserAuthDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessage_ValidRequest_ShouldReturnOk() throws Exception {
        exampleUserEntity.id="1";
        when(userService.findByUsername(eq("user1"))).thenReturn(Optional.of(exampleUserEntity));
        when(messageService.findById(anyString())).thenReturn(Optional.of(new MessageEntity()));

        mockMvc.perform(get("/messages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exampleUserAuthDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void getMessageById_UserNotFound_ShouldReturnBadRequest() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/messages/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exampleUserAuthDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test 
    void getMessageById_MessageNotFound_ShouldReturnBadRequest() throws Exception {
        exampleUserEntity.id="1";
        when(userService.findByUsername(eq("user1"))).thenReturn(Optional.of(exampleUserEntity));
        when(messageService.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/messages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exampleUserAuthDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test 
    void getMessageById_ValidRequest_ShouldReturnOk() throws Exception {
        exampleUserEntity.id="1";
        when(userService.findByUsername(eq("user1"))).thenReturn(Optional.of(exampleUserEntity));
        when(messageService.findById(anyString())).thenReturn(Optional.of(new MessageEntity()));

        mockMvc.perform(get("/messages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exampleUserAuthDTO)))
                .andExpect(status().isOk());
    }

    @Test 
    void deleteMessage_UserNotFound_ShouldReturnUnauthorized() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/messages/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exampleUserAuthDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test 
    void deleteMessage_ValidRequest_ShouldReturnOk() throws Exception {
        exampleUserEntity.id="1";
        when(userService.findByUsername(eq("user1"))).thenReturn(Optional.of(exampleUserEntity));
        doNothing().when(messageService).deleteMessage(anyString());


        mockMvc.perform(delete("/messages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exampleUserAuthDTO)))
                .andExpect(status().isNoContent());
    }



}
