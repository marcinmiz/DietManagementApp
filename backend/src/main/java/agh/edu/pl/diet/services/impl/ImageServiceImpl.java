package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.services.ImageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private final Path productsRoot = Paths.get("images/products");
    private final Path recipesRoot = Paths.get("images/recipes");
    private final Path avatarsRoot = Paths.get("images/avatars");

    @Override
    public void init() {
        try {
            Files.createDirectory(productsRoot);
            Files.createDirectory(recipesRoot);
            Files.createDirectory(avatarsRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile image, String type, Long id) {
        try {
            switch (type) {
                case "product":
                    String productImageName = "product" + id + ".jpg";
                    Path path = this.productsRoot.resolve(productImageName);
                    Files.deleteIfExists(path);
                    Files.copy(image.getInputStream(), path);
                    break;
                case "recipe":
                    String recipeImageName = "recipe" + id + ".jpg";
                    Path path2 = this.recipesRoot.resolve(recipeImageName);
                    Files.deleteIfExists(path2);
                    Files.copy(image.getInputStream(), path2);
                    break;
                case "avatar":
                    String avatarImageName = "avatar" + id + ".jpg";
                    Path path3 = this.avatarsRoot.resolve(avatarImageName);
                    Files.deleteIfExists(path3);
                    Files.copy(image.getInputStream(), path3);
                    break;
                default:
                    throw new RuntimeException("Wrong image type");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = productsRoot.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
