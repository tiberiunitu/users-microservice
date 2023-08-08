package com.softvision.usersmicroservice.service;
import com.softvision.usersmicroservice.dto.UserDTO;
import com.softvision.usersmicroservice.entity.User;
import com.softvision.usersmicroservice.exceptions.UserNotFoundException;
import com.softvision.usersmicroservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private  UserRepository rep;


    public void deleteUserById(Long userId) {
        rep.deleteById(userId);
    }

    public User findUserByEmailAndPassword(String email, String password) {
        User user = rep.findByEmail(email).orElse(null);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
    public UserDTO updateUserByEmailAndPassword(String email, String password, UserDTO userDTO) {
        User existingUser = rep.findByEmailAndPassword(email, password).orElse(null);

        if (existingUser != null) {
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setPassword(userDTO.getPassword());

            var updatedUser =  rep.save(existingUser);

            UserDTO newUser = new UserDTO();
            newUser.setEmail(updatedUser.getEmail());
            newUser.setPassword(updatedUser.getPassword());
            newUser.setFirstName(updatedUser.getFirstName());
            newUser.setLastName(updatedUser.getLastName());

            return newUser;
        }
         else {
             throw new UserNotFoundException("User not found");
        }
    }

    public UserDTO save(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());

        User savedUser = rep.save(user);

        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setEmail(savedUser.getEmail());
        savedUserDTO.setFirstName(savedUser.getFirstName());
        savedUserDTO.setLastName(savedUser.getLastName());
        savedUserDTO.setPassword(savedUser.getPassword());

        return savedUserDTO;
    }



    public List<UserDTO> findAll() {
        try {
            List<User> users = rep.findAll();
            List<UserDTO> userlistdto = users.stream()
                    .map(user -> {
                        UserDTO dto = new UserDTO();
                        dto.setEmail(user.getEmail());
                        dto.setPassword(user.getPassword());
                        dto.setFirstName(user.getFirstName());
                        dto.setLastName(user.getLastName());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return userlistdto;
        } catch (Exception e) {
            e.printStackTrace();
           return null;
        }

    }

    public Long findUserIdByEmailAndPassword(String email, String password) {
        Optional<User> optionalUser = rep.findByEmailAndPassword(email, password);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getUserid();
        }
            return null ;
    }

}

