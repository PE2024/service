package ro.unibuc.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.contact.data.UserEntity;
import ro.unibuc.contact.data.UserRepository;
import ro.unibuc.contact.exception.EntityNotFoundException;

import ro.unibuc.contact.dto.UserDTO;

import java.util.Optional;

@Component
public class UserService {


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
            throw e;
        }
    }

    public UserEntity createUser(UserEntity user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
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
            throw e;
        }
    }

    public void deleteUser(String userId) {
       if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw e;
        } 
    }

}

