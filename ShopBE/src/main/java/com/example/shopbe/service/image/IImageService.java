package com.example.shopbe.service.image;

import com.example.shopbe.dtos.ImageDto;
import com.example.shopbe.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);
    void deleteImage(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
