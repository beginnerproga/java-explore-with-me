package ru.practicum.controllers.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.servers.CategoryService;
import ru.practicum.util.Create;
import ru.practicum.util.Update;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoriesController {
    private final CategoryService categoryService;

    @Autowired
    public AdminCategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDto addCategory(@RequestBody @Validated({Create.class}) CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@Validated({Update.class}) @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public boolean deleteCategory(@PathVariable("catId") long catId) {
        categoryService.deleteCategory(catId);
        return true;
    }
}
