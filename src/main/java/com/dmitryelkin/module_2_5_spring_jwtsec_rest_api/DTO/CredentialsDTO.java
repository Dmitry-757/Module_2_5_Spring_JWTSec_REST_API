package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDTO {

    private String login;
    private String password;

//    public CredentialsDTO() {
//    }
//
//    public CredentialsDTO(String login, String password) {
//        this.login = login;
//        this.password = password;
//    }

//    public String getLogin() {
//        return login;
//    }

//    public void setLogin(String login) {
//        this.login = login;
//    }

//    public String getPassword() {
//        return password;
//    }

//    public void setPassword(String password) {
//        this.password = password;
//    }
}
