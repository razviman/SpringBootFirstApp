package com.example.shopbe.controllers;

import com.example.shopbe.dtos.ImageDto;
import com.example.shopbe.exceptions.ResourceNotFound;
import com.example.shopbe.models.Image;
import com.example.shopbe.response.APIresponse;
import com.example.shopbe.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<APIresponse> saveImages(@RequestParam List<MultipartFile> files,
                                                  @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos =imageService.saveImage(files, productId);
            return ResponseEntity.ok(new APIresponse("Upload Success", imageDtos));
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).
                    body(new APIresponse("Upload Failed", e.getMessage()));
        }

    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,
                (int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName()
                        +"\"").body(resource);
    }

    @PutMapping("image/{imageId}/update")
    public ResponseEntity<APIresponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file) {
        try {
            Image image =  imageService.getImageById(imageId);
            if(image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new APIresponse("Update Success", null));
            }
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIresponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIresponse("Update Failed",
                INTERNAL_SERVER_ERROR));
    }


    @DeleteMapping("image/{imageId}/delete")
    public ResponseEntity<APIresponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image =  imageService.getImageById(imageId);
            if(image != null) {
                imageService.deleteImage(imageId);
                return ResponseEntity.ok(new APIresponse("Delete Success", null));
            }
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIresponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIresponse("Delete Failed",
                INTERNAL_SERVER_ERROR));
    }
}
