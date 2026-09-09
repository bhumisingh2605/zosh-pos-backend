package com.zosh.zosh.pos.system.controller;

import com.zosh.zosh.pos.system.domain.StoreStatus;
import com.zosh.zosh.pos.system.exceptions.UserException;
import com.zosh.zosh.pos.system.mapper.StoreMapper;
import com.zosh.zosh.pos.system.modal.Store;
import com.zosh.zosh.pos.system.modal.User;
import com.zosh.zosh.pos.system.payload.dto.StoreDto;
import com.zosh.zosh.pos.system.payload.response.ApiResponse;
import com.zosh.zosh.pos.system.service.StoreService;
import com.zosh.zosh.pos.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

      @PostMapping
    public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto,
                                                 @RequestHeader("Authorization" )String jwt) throws UserException {

        User user = userService.getUserFromJwtToken(jwt);
       return ResponseEntity.ok(storeService.createStore(storeDto, user));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long id,
                                                @RequestHeader("Authorization" )String jwt) throws Exception {

          return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllStore(
                                                 @RequestHeader("Authorization" )String jwt) throws Exception {

        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/admin")
    public ResponseEntity<StoreDto> getStoreByAdmin(
            @RequestHeader("Authorization" )String jwt) throws Exception {

        return ResponseEntity.ok(StoreMapper.toDto(storeService.getStoreByAdmin()));
    }

    @GetMapping("/employee")
    public ResponseEntity<StoreDto> getStoreByEmployee(
            @RequestHeader("Authorization" )String jwt) throws Exception {

        return ResponseEntity.ok(storeService.getStoreByEmployee());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long id,
                                                @RequestBody StoreDto storeDto) throws Exception {
          return ResponseEntity.ok(storeService.updateStore(id, storeDto));
    }

    @PutMapping("/{id}/moderate")
    public ResponseEntity<StoreDto> moderateStore(@PathVariable Long id,
                                                @RequestBody StoreStatus status) throws Exception {
        return ResponseEntity.ok(storeService.moderateStore(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStore(@PathVariable Long id) throws Exception {
          storeService.deleteStore(id);
          ApiResponse apiResponse = new ApiResponse();
          apiResponse.setMessage("Store deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }


}
