package com.zosh.zosh.pos.system.payload.dto;

import com.zosh.zosh.pos.system.modal.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    //private Store store;
    private Long storeId;
}
