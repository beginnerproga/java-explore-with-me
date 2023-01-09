package ru.practicum.servers;

import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto getCategory(long catId);

    List<CategoryDto> getCategories(int from, int size);
}
