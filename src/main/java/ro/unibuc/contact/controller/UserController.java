package ro.unibuc.contact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.contact.dto.CreateUserResponse;
import ro.unibuc.contact.dto.UpdateUserPasswordRequest;
import ro.unibuc.contact.service.UserService;
import ro.unibuc.contact.data.UserEntity;
import ro.unibuc.contact.exception.EntityNotFoundException;

import javax.websocket.server.PathParam;
import java.util.Optional;

@Controller
public class UserController{

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {
        Optional<UserEntity> existingUser = userService.findByUsername(user.username);
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
       
        UserEntity newUser;
        try {
            newUser = userService.createUser(user);
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
        CreateUserResponse response = new CreateUserResponse(newUser.id, "User created successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserEntity user) {
        Optional<UserEntity> existingUser = userService.findById(userId);
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username does not exist");
        }

        try {
            user.id = userId;
            return ResponseEntity.ok(userService.updateUser(user));
        } catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<?> updateUserPassword(@PathVariable String userId, @RequestBody UpdateUserPasswordRequest updateUserPasswordRequest) {
        Optional<UserEntity> existingUser = userService.findById(userId);
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username does not exist");
        }

        try {
            UserEntity existingUserEntity = existingUser.get();
            existingUserEntity.password = updateUserPasswordRequest.getPassword();
            System.out.println(updateUserPasswordRequest.getPassword());
            return ResponseEntity.ok(userService.updateUser(existingUserEntity));
        } catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
         try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

