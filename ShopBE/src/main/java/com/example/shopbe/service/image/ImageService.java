package com.example.shopbe.service.image;

import com.example.shopbe.dtos.ImageDto;
import com.example.shopbe.exceptions.ResourceNotFound;
import com.example.shopbe.models.Image;
import com.example.shopbe.models.Product;
import com.example.shopbe.repository.image.ImageRepository;
import com.example.shopbe.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Image doesnt exists!"));
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                () -> {throw new ResourceNotFound("Image doesnt exists!");});
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImagesDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl ="/api/v1/images/image/download";
                String downloadUrl = buildDownloadUrl + image.getId();

                image.setDownloadURL(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadURL(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadURL(savedImage.getDownloadURL());
                savedImagesDto.add(imageDto);


            } catch (SerialException e) {
                throw new RuntimeException(e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(()
                -> new ResourceNotFound("Image doesn't exists!"));
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
