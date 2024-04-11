package ro.unibuc.contact.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.cucumber.java.id.Diasumsikan;
import ro.unibuc.contact.data.InformationEntity;
import ro.unibuc.contact.data.UserEntity;
import ro.unibuc.contact.data.UserRepository;
import ro.unibuc.contact.dto.Greeting;
import ro.unibuc.contact.dto.UserDTO;
import ro.unibuc.contact.exception.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.xml.bind.annotation.W3CDomHandler;

@ExtendWith(SpringExtension.class)
class UserServiceTest{

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService= new UserService();

    private UserDTO userDTO;
    private UserEntity userEntity;


    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("username", "email@example.com", "password");
        userEntity = new UserEntity("username", "email@example.com", "password");
    }

    @Disabled
    @Test
    void testCreateUser() {
        String username = "user1234";
        String email = "user@mail.com";
        String password = "password";

        UserEntity newUser = userService.createUser(userEntity);
        
        Assertions.assertEquals(password, newUser.password);
        Assertions.assertEquals(username, newUser.username);
        Assertions.assertEquals(email, newUser.email);
    }

    @Test
    void updateUser_returnsUpdatedUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity updatedUser = userService.updateUser(userEntity);

        assertEquals(userEntity.username, updatedUser.username);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test 
    void createUserByDTO_returnsCreatedUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity result = userService.createUserByDTO(userDTO);

        assertEquals(userEntity.username, result.username);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }


    @Test
    void findByUsername_found_returnsUser() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.findByUsername("username");

        assertEquals(userEntity.username, result.get().username);
    }

    @Test 
    void findByUsername_notFound_returnsEmpty() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findByUsername("username");

        assertEquals(Optional.empty(), result);
    }

    @Test 
    void findById_found_returnsUser() {
        when(userRepository.findById("id")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.findById("id");

        assertEquals(userEntity.username, result.get().username);
    }

    @Test 
    void findById_notFound_returnsEmpty() {
        when(userRepository.findById("id")).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findById("id");

        assertEquals(Optional.empty(), result);
    }

    @Test 
    void deleteUser_userExists_deletesUser() {
        when(userRepository.existsById("id")).thenReturn(true);

        userService.deleteUser("id");

        verify(userRepository, times(1)).deleteById("id");
    }

    @Test
    void deleteUser_userDoesNotExist_throwsEntityNotFoundException() {
        when(userRepository.existsById("id")).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser("id"));
    }

}