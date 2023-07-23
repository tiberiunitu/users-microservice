package com.softvision.usersmicroservice.controller;
import com.softvision.usersmicroservice.dto.UserDTO;
import com.softvision.usersmicroservice.repo.UserRepository;
import com.softvision.usersmicroservice.service.UserService;
import com.softvision.usersmicroservice.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/demo")
public class UserController {
    @Autowired
    private UserRepository userRep;
    @Autowired
    private UserService service;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password) {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPassword(password);
        //it gives me error if I don't have try catch or if is not commented

        try {
            userRep.save(u);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return "Saved";
    }

    //there is an error in return statement regarding ?? userid I guess
    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<User> users = userRep.findAll();
            List<UserDTO> userlistdto = users.stream()
                    .map(user -> {
                        UserDTO dto = new UserDTO();
                        dto.setEmail(user.getEmail());
                        dto.setPassword(user.getPassword());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userlistdto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping(path="/GetMeTheUser")
    public Long findUserByEmailAndPassword(String email, String password) {
        User user = userRep.findByEmail(email).orElse(null);

        if (user != null && user.getPassword().equals(password)) {
            return user.getUserid();
        }

        return null;
    }


    @PutMapping(path = "/update")
    public ResponseEntity<String> updateUserByEmailAndPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("newFirstName") String newFirstName,
            @RequestParam("newLastName") String newLastName,
            @RequestParam("newEmail") String newEmail,
            @RequestParam("newPassword") String newPassword
    ) {
        try {
            service.updateUserByEmailAndPassword(email, password, newFirstName, newLastName, newEmail, newPassword);
            return ResponseEntity.ok("Updated!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
     @DeleteMapping(path = "/delete")
        public ResponseEntity<String> deleteUserByEmailAndPassword(
                @RequestParam("email") String email,
                @RequestParam("password") String password) {
            User user = service.findUserByEmailAndPassword(email, password);

            if (user!= null) {
                service.deleteUserById(user.getUserid());
                return ResponseEntity.ok("Deleted");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
            }
        }
    }







