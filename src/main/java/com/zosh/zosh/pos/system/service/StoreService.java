package com.zosh.zosh.pos.system.service;

import com.zosh.zosh.pos.system.domain.StoreStatus;
import com.zosh.zosh.pos.system.exceptions.UserException;
import com.zosh.zosh.pos.system.modal.Store;
import com.zosh.zosh.pos.system.modal.User;
import com.zosh.zosh.pos.system.payload.dto.StoreDto;

import java.util.List;

public interface StoreService {

      StoreDto createStore(StoreDto storeDto, User user);
      StoreDto getStoreById(Long id) throws Exception;
      List<StoreDto> getAllStores();
      Store getStoreByAdmin() throws UserException;
      StoreDto updateStore(Long id, StoreDto storeDto) throws Exception;
      void deleteStore(Long id) throws UserException;
      StoreDto getStoreByEmployee() throws UserException;

      StoreDto moderateStore(Long id, StoreStatus status) throws Exception;
}
