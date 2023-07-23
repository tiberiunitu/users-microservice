package com.softvision.usersmicroservice.service;
import com.softvision.usersmicroservice.entity.User;
import com.softvision.usersmicroservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private final UserRepository rep;
    @Autowired
    public UserService (UserRepository rep){
        this.rep=rep;
    }

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
    public void updateUserByEmailAndPassword(String email, String password, String newFirstName, String newLastName, String newEmail, String newPassword) {
        User existingUser = rep.findByEmailAndPassword(email, password).orElse(null);

        if (existingUser != null) {
            existingUser.setFirstName(newFirstName);
            existingUser.setLastName(newLastName);
            existingUser.setEmail(newEmail);
            existingUser.setPassword(newPassword);

            rep.save(existingUser);
        }


    }

}

