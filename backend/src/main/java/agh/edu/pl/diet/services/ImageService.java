package agh.edu.pl.diet.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void init();

    void save(MultipartFile file, String type, Long id);

    Resource load(String filename);

}
