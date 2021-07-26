package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.Category;
import agh.edu.pl.diet.repos.CategoryRepo;
import agh.edu.pl.diet.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        categoryRepo.findAll().forEach(list::add);
        return list;
    }
}
