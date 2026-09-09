package com.zosh.zosh.pos.system.service.impl;

import com.zosh.zosh.pos.system.domain.StoreStatus;
import com.zosh.zosh.pos.system.exceptions.UserException;
import com.zosh.zosh.pos.system.mapper.StoreMapper;
import com.zosh.zosh.pos.system.modal.Store;
import com.zosh.zosh.pos.system.modal.StoreContact;
import com.zosh.zosh.pos.system.modal.User;
import com.zosh.zosh.pos.system.payload.dto.StoreDto;
import com.zosh.zosh.pos.system.repository.StoreRepository;
import com.zosh.zosh.pos.system.service.StoreService;
import com.zosh.zosh.pos.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;

    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {

        Store store = StoreMapper.toEntity(storeDto, user);
        return StoreMapper.toDto(storeRepository.save(store));
    }

    @Override
    public StoreDto getStoreById(Long id) throws Exception {

        Store store = storeRepository.findById(id).orElseThrow(
                ()-> new Exception("store not found")
        );
        return StoreMapper.toDto(store);
    }

    @Override
    public List<StoreDto> getAllStores() {
        List<Store> dtos =  storeRepository.findAll();
        return dtos.stream().map(StoreMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public Store getStoreByAdmin() throws UserException {
        User admin= userService.getCurrentUser();
        return storeRepository.findByStoreAdminId(admin.getId());
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) throws Exception {
        User currentUser = userService.getCurrentUser();

        Store existing = storeRepository.findByStoreAdminId(currentUser.getId());

        if(existing == null) {
            throw new Exception("store not found");
        }

        existing.setBrand(storeDto.getBrand());
        existing.setDescription(storeDto.getDescription());
        if(storeDto.getStoreType() != null) {
           existing.setStoreType(storeDto.getStoreType());
        }
         if(storeDto.getContact() != null) {
             StoreContact contact = StoreContact.builder()
                     .address(storeDto.getContact().getAddress())
                     .phone(storeDto.getContact().getPhone())
                     .email(storeDto.getContact().getEmail())
                     .build();
             existing.setContact(contact);
         }
         Store updatedStore = storeRepository.save(existing);

        return StoreMapper.toDto(updatedStore);
    }

    @Override
    public void  deleteStore(Long id) throws UserException {
        Store store = getStoreByAdmin();

        storeRepository.delete(store);
    }

    @Override
    public StoreDto getStoreByEmployee() throws UserException {
        User currentUser = userService.getCurrentUser();
        if(currentUser == null) {
            throw new UserException("you don't have permission to access this store");
        }
        return StoreMapper.toDto(currentUser.getStore());
    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus status) throws Exception {
        Store store = storeRepository.findById(id).orElseThrow(
                ()-> new Exception("Store not found")
        );

        store.setStatus(status);
        Store updatedStore=storeRepository.save(store);
        return StoreMapper.toDto(updatedStore);
    }
}
