package ru.practicum.controllers.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.servers.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")

public class PublicCategoriesController {
    private final CategoryService categoryService;

    @Autowired
    public PublicCategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable("catId") long catId) {
        return categoryService.getCategory(catId);
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from, @RequestParam(required = false, defaultValue = "10") int size) {
        return categoryService.getCategories(from, size);
    }
}
