package com.lms.hat.thinking.repository;

import com.lms.hat.thinking.model.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material findByName(String name);
}
