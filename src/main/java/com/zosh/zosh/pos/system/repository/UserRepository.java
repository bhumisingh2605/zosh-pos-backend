package com.zosh.zosh.pos.system.repository;

import com.zosh.zosh.pos.system.modal.Store;
import com.zosh.zosh.pos.system.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByStore(Store store);
    List<User> findByBranchId(Long branchId);

}
