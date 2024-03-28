package ro.unibuc.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.contact.data.UserEntity;
import ro.unibuc.contact.data.UserRepository;
import ro.unibuc.contact.exception.EntityNotFoundException;

import ro.unibuc.contact.dto.UserDTO;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    
    public UserEntity createUserByDTO(UserDTO user) {
        try{
        UserEntity userEntity = new UserEntity();
        userEntity.email = user.getEmail();
        userEntity.username = user.getUsername();
        userEntity.password = user.getPassword();
        return createUser(userEntity);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            throw e;
        }
    }

    public UserEntity createUser(UserEntity user) {
        try {

            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findById(String id) {
        return userRepository.findById(id);
    }

    public UserEntity updateUser(UserEntity user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void deleteUser(String userId) {
       if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        try {
            log.info("Deleting user with id: {}", userId);
            userRepository.deleteById(userId);
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage(), e);
            throw e;
        } 
    }

}

