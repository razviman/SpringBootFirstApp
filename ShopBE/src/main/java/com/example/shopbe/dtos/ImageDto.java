package com.example.shopbe.dtos;


import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private String fileName;
    private String downloadURL;
}
