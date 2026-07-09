package com.qubikore.assetsteward.category;

import com.qubikore.assetsteward.common.exception.ResourceNotFoundException;
import com.qubikore.assetsteward.common.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    public CategoryServiceImpl(CategoryRepository categoryRepository, FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        
        if (categoryRequest.getIcon() != null && !categoryRequest.getIcon().isEmpty()) {
            String iconUrl = fileStorageService.storeFile(categoryRequest.getIcon(), "categories");
            category.setIconName(iconUrl);
        }

        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (categoryRequest.getName() != null && !categoryRequest.getName().trim().isEmpty()) {
            category.setName(categoryRequest.getName());
        }

        if (categoryRequest.getDescription() != null && !categoryRequest.getDescription().trim().isEmpty()) {
            category.setDescription(categoryRequest.getDescription());
        }
        
        if (categoryRequest.getIcon() != null && !categoryRequest.getIcon().isEmpty()) {
            String iconUrl = fileStorageService.storeFile(categoryRequest.getIcon(), "categories");
            category.setIconName(iconUrl);
        }

        Category updatedCategory = categoryRepository.save(category);
        return mapToDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        
        if (category.getIconName() != null && !category.getIconName().startsWith("http")) {
            dto.setIconName(backendUrl + category.getIconName());
        } else {
            dto.setIconName(category.getIconName());
        }
        
        return dto;
    }
}
