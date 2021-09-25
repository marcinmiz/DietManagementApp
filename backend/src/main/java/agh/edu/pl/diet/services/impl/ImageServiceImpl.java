package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.controllers.ImageController;
import agh.edu.pl.diet.services.ImageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

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
    public Resource load(String type, String filename) {
        try {
            Path file;

            switch (type) {
                case "product":
                    file = productsRoot.resolve(filename);
                    break;
                case "recipe":
                    file = recipesRoot.resolve(filename);
                    break;
                case "avatar":
                    file = avatarsRoot.resolve(filename);
                    break;
                default:
                    throw new RuntimeException("Wrong image type");
            }

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

    @Override
    public String getImageURL(String item_type, Long itemId) {

        Path root;
        String url = "";
        String filename = item_type.toLowerCase() + itemId + ".jpg";

        switch (item_type.toLowerCase()) {
            case "product":
                root = productsRoot;
                break;
            case "recipe":
                root = recipesRoot;
                break;
            case "avatar":
                root = avatarsRoot;
                break;
            default:
                throw new RuntimeException("Wrong item type");
        }

        Path file = root.resolve(filename);

        try {
            List<String> list2 = Files.walk(root, 1).filter(path -> !path.equals(root) && path.getFileName().toString().equals(filename)).map(root::relativize).map(path -> path.getFileName().toString()).collect(Collectors.toList());
            if (!list2.isEmpty()) {
                url = MvcUriComponentsBuilder
                        .fromMethodName(ImageController.class, "getFile", item_type, file.getFileName().toString()).build().toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return url;
    }

    @Override
    public void removeImageIfExists(String item_type, Long itemId) {
        try {
            String filename = item_type.toLowerCase() + itemId + ".jpg";

            switch (item_type.toLowerCase()) {
                case "product":
                    Path path = this.productsRoot.resolve(filename);
                    Files.deleteIfExists(path);
                    break;
                case "recipe":
                    Path path2 = this.recipesRoot.resolve(filename);
                    Files.deleteIfExists(path2);
                    break;
                case "avatar":
                    Path path3 = this.avatarsRoot.resolve(filename);
                    Files.deleteIfExists(path3);
                    break;
                default:
                    throw new RuntimeException("Wrong image type");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not remove the file. Error: " + e.getMessage());
        }
    }
}
