package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.Branch;
import com.zosh.zosh.pos.system.modal.Inventory;
import com.zosh.zosh.pos.system.modal.Product;
import com.zosh.zosh.pos.system.payload.dto.InventoryDto;

public class InventoryMapper {

    public static InventoryDto toDTO(Inventory inventory) {
        return InventoryDto.builder()
                .id(inventory.getId())
                .branchId(inventory.getBranch().getId())
                .productId(inventory.getProduct().getId())
                .product(ProductMapper.toDTO(inventory.getProduct()))
                .quantity(inventory.getQuantity())
                .build();

    }
    public static Inventory toEntity(InventoryDto inventoryDto, Branch branch,
                                     Product product) {
       return Inventory.builder()
               .branch(branch)
               .product(product)
               .quantity(inventoryDto.getQuantity())
               .build();

    }
}
