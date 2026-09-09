package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.Category;
import com.zosh.zosh.pos.system.payload.dto.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toDTO(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .storeId(category.getId() != null ? category.getStore().getId():null)
                .build();

    }
}
