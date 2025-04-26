package ru.itis.inf304.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
}