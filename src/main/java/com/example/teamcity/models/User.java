package com.example.teamcity.models;

import com.example.teamcity.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class User extends BaseModel {
    @Random
    private String user;
    @Random
    private String password;
    private Roles role;


    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUsername() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
