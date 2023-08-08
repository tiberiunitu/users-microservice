package com.softvision.usersmicroservice.service;

import com.softvision.usersmicroservice.dto.UserDTO;
import com.softvision.usersmicroservice.entity.User;
import com.softvision.usersmicroservice.exceptions.UserNotFoundException;
import com.softvision.usersmicroservice.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository rep;

    @InjectMocks
    private UserService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void reset(){
        Mockito.reset(rep);
    }

    @Test
    public void testDeleteUserById() {
        Long userId = 123L;

        service.deleteUserById(userId);

        verify(rep, times(1)).deleteById(userId);
    }

    @Test
    public void testFindUserByEmailAndPass_Valid(){
        String email= "email@email.com";
        String password="pass123";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        when(rep.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = service.findUserByEmailAndPassword(email, password);

        verify(rep, times(1)).findByEmail(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        assertEquals(password, foundUser.getPassword());
    }

    @Test
    public void testFindUserByEmailAndPassword_Invalid(){

        String email= "email@email.com";
        String password="pass123";

        when(rep.findByEmail(email)).thenReturn(Optional.empty());
        User foundUser = service.findUserByEmailAndPassword(email, password);
        verify(rep, times(1)).findByEmail(email);
        assertNull(foundUser);


    }

    @Test
    public void testUpdateUserByEmailAndPass_Success(){
        String email = "test@email.com";
        String password = "test-pass";

        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setPassword(password);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("updated@example.com");
        userDTO.setPassword("updatedPassword");
        userDTO.setFirstName("UpdatedFirstName");
        userDTO.setLastName("UpdatedLastName");

        when(rep.findByEmailAndPassword(email, password)).thenReturn(Optional.of(existingUser));

        when(rep.save(existingUser)).thenReturn(existingUser);

        UserDTO updatedUserDTO = service.updateUserByEmailAndPassword(email, password, userDTO);

        verify(rep, times(1)).findByEmailAndPassword(email, password);

        verify(rep, times(1)).save(existingUser);

        assertNotNull(updatedUserDTO);
        assertEquals(userDTO.getEmail(), updatedUserDTO.getEmail());
        assertEquals(userDTO.getPassword(), updatedUserDTO.getPassword());
        assertEquals(userDTO.getFirstName(), updatedUserDTO.getFirstName());
        assertEquals(userDTO.getLastName(), updatedUserDTO.getLastName());
    }

    @Test
    public void testUpdateUserByEmailAndPassword_Fail(){
        String email = "test@email.com";
        String password = "test-pass";

        UserDTO dto= new UserDTO();
        dto.setFirstName("updatedFirstName");
        dto.setLastName("updatedLastName");
        dto.setEmail("update@email.com");
        dto.setPassword("pass-updated");

        when(rep.findByEmailAndPassword(email,password)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.updateUserByEmailAndPassword(email, password, dto));
        verify(rep, times(1)).findByEmailAndPassword(email, password);
        verify(rep, never()).save(any());

    }

    @Test
    public void testSave_Success(){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@email.com");
        userDTO.setPassword("testPassword");
        userDTO.setFirstName("TestFirstName");
        userDTO.setLastName("TestLastName");

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        when(rep.save(any())).thenReturn(user);

        UserDTO savedUserDTO = service.save(userDTO);
        verify(rep, times(1)).save(any());

        assertNotNull(savedUserDTO);
        assertEquals(userDTO.getEmail(), savedUserDTO.getEmail());
        assertEquals(userDTO.getPassword(), savedUserDTO.getPassword());
        assertEquals(userDTO.getFirstName(), savedUserDTO.getFirstName());
        assertEquals(userDTO.getLastName(), savedUserDTO.getLastName());

    }

    @Test
    public void testFindAll_Success(){
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        userList.add(user1);

        when(rep.findAll()).thenReturn(userList);
        List<UserDTO>dtoList=service.findAll();
        verify(rep, times(1)).findAll();

        assertEquals(userList.size(), dtoList.size());

        UserDTO userDTO1 = dtoList.get(0);
        assertEquals(user1.getEmail(), userDTO1.getEmail());
        assertEquals(user1.getPassword(), userDTO1.getPassword());
        assertEquals(user1.getFirstName(), userDTO1.getFirstName());
        assertEquals(user1.getLastName(), userDTO1.getLastName());
    }

    @Test
    public void testFindAll_Catch(){
        when(rep.findAll()).thenThrow(new UserNotFoundException("Error finding users"));
        List<UserDTO> dtoList = service.findAll();
        verify(rep, times(1)).findAll();
        assertNull(dtoList);
    }
    @Test
    public void testFindUserByEmailAndPassword(){
        String email="email@email.com";
        String password="pass12345";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUserid(123L);

        when(rep.findByEmailAndPassword(email,password)).thenReturn(Optional.of(user));
        Long userid=service.findUserIdByEmailAndPassword(email,password);
        verify(rep, times(1)).findByEmailAndPassword(email, password);
        assertNotNull(userid);
        assertEquals(user.getUserid(), userid);
    }

    @Test
    public void testFindUserByEmailAndPassword_Unsuccessful(){
        String email="email@email.com";
        String password="pass12345";

        when(rep.findByEmailAndPassword(email,password)).thenReturn(Optional.empty());
        Long foundUserId = service.findUserIdByEmailAndPassword(email, password);
        verify(rep, times(1)).findByEmailAndPassword(email, password);
        assertNull(foundUserId);

    }

}




