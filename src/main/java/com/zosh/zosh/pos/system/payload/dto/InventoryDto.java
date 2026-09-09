package com.zosh.zosh.pos.system.payload.dto;

import com.zosh.zosh.pos.system.modal.Branch;
import com.zosh.zosh.pos.system.modal.Product;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {

    private Long id;

    private BranchDto branch;

    private Long branchId;

    private Long productId;

    private ProductDto product;

    private Integer quantity;

    private LocalDateTime lastUpdate;

}
