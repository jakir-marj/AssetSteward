package com.qubikore.assetsteward.category;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryRequest categoryRequest);
    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(Long id, CategoryRequest categoryRequest);
    void deleteCategory(Long id);
}
