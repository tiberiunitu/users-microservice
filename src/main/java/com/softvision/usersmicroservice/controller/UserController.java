package com.softvision.usersmicroservice.controller;
import com.softvision.usersmicroservice.dto.UserDTO;
import javax.ws.rs.*;

import com.softvision.usersmicroservice.service.UserService;
import com.softvision.usersmicroservice.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/users")
public class UserController {

    @Autowired
    private UserService service;
    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping(path = "/add", consumes="application/json", produces="application/json")
    public @ResponseBody ResponseEntity<UserDTO> addNewUser(@RequestBody UserDTO dto) {
        UserDTO savedUser = service.save(dto);

        if (savedUser != null) {
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/getUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = service.findAll();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping(path="/getMeTheUser")
    public ResponseEntity<Long>  findUserByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
        Long userId = service.findUserIdByEmailAndPassword(email, password);
        if (userId != null) {
            ResponseEntity.ok(HttpStatus.resolve(201));
//            return userId;
            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        } else {
           return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }



        @PutMapping(path = "/update")
    public ResponseEntity<UserDTO> updateUserByEmailAndPassword(
            @QueryParam("email") String email,
            @QueryParam("password") String password,
            @RequestBody UserDTO userDTO) {
        try {
            var newUser = service.updateUserByEmailAndPassword(email, password, userDTO);
            if(newUser != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
     @DeleteMapping(path = "/delete")
        public ResponseEntity<UserDTO> deleteUserByEmailAndPassword(
                @RequestParam("email") String email,
                @RequestParam("password") String password) {


         try {
             var user = service.findUserByEmailAndPassword(email, password);
             if (user!=null){
                 service.deleteUserById(user.getUserid());
                 return ResponseEntity.status(HttpStatus.OK).build();
             }
             else {
                 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
             }

         } catch (EntityNotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
         }
     }
    }







