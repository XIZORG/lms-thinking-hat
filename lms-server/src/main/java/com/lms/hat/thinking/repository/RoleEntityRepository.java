package com.lms.hat.thinking.repository;

import com.lms.hat.thinking.model.user.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
}
