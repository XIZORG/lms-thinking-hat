package com.lms.hat.thinking.service;

import com.lms.hat.thinking.model.material.MaterialRequest;
import com.lms.hat.thinking.model.material.MaterialResponse;
import com.lms.hat.thinking.model.material.Material;

import java.util.List;

public interface MaterialService {
    MaterialResponse save(MaterialRequest mr, String login);
    List<MaterialResponse> findAll();
    MaterialResponse findByName(String name);
    MaterialResponse update(Long id, MaterialRequest mr, String userLogin);
    void deleteMaterial(Long id, String userLogin);
}
