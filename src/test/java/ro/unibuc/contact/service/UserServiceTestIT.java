package ro.unibuc.contact.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.contact.data.InformationRepository;
import ro.unibuc.contact.data.UserRepository;
import ro.unibuc.contact.dto.Greeting;

@SpringBootTest
@Tag("IT")
class UserServiceTestIT{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
}