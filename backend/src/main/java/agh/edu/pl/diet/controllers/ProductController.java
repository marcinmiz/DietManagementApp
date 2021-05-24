package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/new/edit", method = RequestMethod.GET)
    public String addProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "new/edit";
    }

    @RequestMapping(value = "/new/edit", method = RequestMethod.POST)
    public String addProductPost(@ModelAttribute("product") Product product, HttpServletRequest request) {
        productService.save(product);


        MultipartFile prductImage = product.getProductImage();

        try {
            byte[] bytes = prductImage.getBytes();
            String name = product.getId() + ".png";
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File("frontend/src/images/" + name)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:products";
    }

    @RequestMapping("/productInfo")
    public String productInfo(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);

        return "productInfo";
    }

    @RequestMapping("/updateProduct")
    public String updateProduct(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);

        return "updateProduct";
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProductPost(@ModelAttribute("product") Product product, HttpServletRequest request) {
        productService.save(product);

        MultipartFile productImage = product.getProductImage();

        if (!productImage.isEmpty()) {
            try {
                byte[] bytes = productImage.getBytes();
                String name = product.getId() + ".png";

                Files.delete(Paths.get("frontend/src/images/" + name));

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File("frontend/src/images/" + name)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/product/productInfo?id=" + product.getId();
    }

    @RequestMapping("/productList")
    public String productList(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        return "productList";

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(
            @ModelAttribute("id") String id, Model model
    ) {
        productService.removeOne(Long.parseLong(id.substring(8)));
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);

        return "redirect:/product/productList";
    }

}