package com.zosh.zosh.pos.system.repository;

import com.zosh.zosh.pos.system.modal.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByStoreAdminId(Long adminId);
}
