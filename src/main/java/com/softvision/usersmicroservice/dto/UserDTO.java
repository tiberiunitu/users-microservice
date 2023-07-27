package com.softvision.usersmicroservice.dto;
public class UserDTO {

    private String email;

    private String firstName;
    private String lastName;

    private String password;

    public void setEmail(String email){

        this.email = email;
    }
    public String getEmail(){
        return email;

    }
    public void setPassword(String password){
        this.password =password;
    }
    public String getPassword(){
        return password;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDTO(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public UserDTO() {
    }
}
