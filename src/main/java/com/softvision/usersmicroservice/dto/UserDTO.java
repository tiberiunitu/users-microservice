package com.softvision.usersmicroservice.dto;
public class UserDTO {

    private String emailofuser;
    private String passwordOfUser;

    public void setEmail(String emailofuser){
        this.emailofuser=emailofuser;
    }
    public String getEmail(){
        return emailofuser;

    }
    public void setPassword(String passwordOfUser){
        this.passwordOfUser=passwordOfUser;
    }
    public String getPassword(){
        return passwordOfUser;
    }

}
