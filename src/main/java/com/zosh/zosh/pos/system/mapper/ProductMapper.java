package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.Category;
import com.zosh.zosh.pos.system.modal.Product;
import com.zosh.zosh.pos.system.modal.Store;
import com.zosh.zosh.pos.system.payload.dto.ProductDto;

public class ProductMapper {

    public static ProductDto toDTO(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .mrp(product.getMrp())
                .sellingPrice(product.getSellingPrice())
                .brand(product.getBrand())
                .category(CategoryMapper.toDTO(product.getCategory()))
                .storeId(product.getStore() != null ? product.getStore().getId():null)
                .image(product.getImage())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
              //  .categoryId(prodpuct.get)

    }

    public static Product toEntity(ProductDto productDto, Store store,
                                   Category category) {
        return Product.builder()
                .name(productDto.getName())
                .store(store)
                .category(category)
                .sku(productDto.getSku())
                .description(productDto.getDescription())
                .mrp(productDto.getMrp())
                .sellingPrice(productDto.getSellingPrice())
                .brand(productDto.getBrand())
                .build();

    }
}
