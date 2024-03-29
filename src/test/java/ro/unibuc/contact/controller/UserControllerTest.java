package ro.unibuc.contact.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.contact.dto.UserDTO;
import ro.unibuc.contact.dto.CreateUserResponse;
import ro.unibuc.contact.dto.UpdateUserPasswordRequest;
import ro.unibuc.contact.exception.EntityNotFoundException;
import ro.unibuc.contact.service.UserService;
import ro.unibuc.contact.data.UserEntity;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    UserDTO userDTO = new UserDTO("username", "email@example.com", "password");
     UserEntity userEntity = new UserEntity("username", "email@example.com", "password");


    @Test
    void createUser_successful() throws Exception {
        when(userService.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userService.createUserByDTO(any(UserDTO.class))).thenReturn(userEntity);
        when(userService.createUser(userEntity)).thenReturn(userEntity);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_duplicateUsername() throws Exception {
        when(userService.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(userEntity));

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test 
    void createUser_internalServerError() throws Exception {
        when(userService.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userService.createUserByDTO(any(UserDTO.class))).thenReturn(userEntity);
        doThrow(new RuntimeException()).when(userService).createUserByDTO(any(UserDTO.class));


        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test 
    void updateUser_userNotFound() throws Exception {
        when(userService.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1")
                        .content(objectMapper.writeValueAsString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test 
    void updateUser_successful() throws Exception {
        when(userService.findById("1")).thenReturn(Optional.of(userEntity));
        when(userService.updateUser(userEntity)).thenReturn(userEntity);

        mockMvc.perform(put("/users/1")
                        .content(objectMapper.writeValueAsString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test 
    void updateUserPassword_userNotFound() throws Exception {
        when(userService.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(new UpdateUserPasswordRequest("password")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserPassword_successful() throws Exception {
        when(userService.findById("1")).thenReturn(Optional.of(userEntity));
        when(userService.updateUser(userEntity)).thenReturn(userEntity);

        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(new UpdateUserPasswordRequest("password")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test 
    void deleteUser_successful() throws Exception {
        doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test 
    void deleteUser_userNotFound() throws Exception {
        doThrow(new EntityNotFoundException("")).when(userService).deleteUser("1");

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }

    

}