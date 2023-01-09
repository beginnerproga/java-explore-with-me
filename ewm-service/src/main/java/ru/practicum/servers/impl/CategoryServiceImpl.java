package ru.practicum.servers.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exceptions.SameNameException;
import ru.practicum.exceptions.exception404.CategoryNotFoundException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.servers.CategoryService;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@Getter
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info("Received request to add category");
        Category category = CategoryMapper.toCategory(categoryDto);
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new SameNameException("Category with this name already created");
        }
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto getCategory(long catId) {
        log.info("Received request to get category");
        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            throw new CategoryNotFoundException("Category with id = " + catId + " not found");
        });
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("Received request to get all categories");
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        log.info("Received request to delete a user by userId={}", catId);
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
        {
            throw new CategoryNotFoundException("Category with id = " + catId + " not found");
        });
        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        log.info("Received request to update a category with id={}", categoryDto.getId());
        if (categoryDto.getId() == null)
            throw new ValidationException("Category id's cannot be null");
        Category category = CategoryMapper.toCategory(categoryDto);
        Category result = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> {
            throw new CategoryNotFoundException("Category with id=" + categoryDto.getId() + " not found");
        });
        if (category.getName() != null && !category.getName().isBlank())
            result.setName(category.getName());
        try {
            categoryRepository.save(result);
            return CategoryMapper.toCategoryDto(result);
        } catch (Exception e) {
            throw new SameNameException("Category with this name already created");
        }
    }
}
