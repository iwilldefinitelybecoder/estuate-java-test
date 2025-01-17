package com.estuate.project1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor

public class AuthResponse {
    private  String jwt;
    private  String email;
    private String name;
    private String pictureUrl;
    private  Long userId;
    private  List<String> interests;
}