package com.lms.hat.thinking.service.impl;

import com.lms.hat.thinking.exception.status.RestUnauthorizedException;
import com.lms.hat.thinking.model.material.MaterialRequest;
import com.lms.hat.thinking.model.material.MaterialResponse;
import com.lms.hat.thinking.model.material.Material;
import com.lms.hat.thinking.exception.model.FieldErrorModel;
import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.repository.MaterialRepository;
import com.lms.hat.thinking.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public MaterialResponse save(MaterialRequest request, String login){
        Material material = new Material();
        material.setLink(request.getLink());
        material.setName(request.getName());
        material.setCreator(login);

        if(materialRepository.findByName(request.getName()) != null) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("name", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "duplicate material name!")));
        }

        return MaterialResponse.responseFromMaterial(materialRepository.save(material));
    }

    @Override
    public List<MaterialResponse> findAll(){
        return materialToResponse(materialRepository.findAll());
    }

    @Override
    public MaterialResponse findByName(String name) {
        Material material = materialRepository.findByName(name);
        if (material == null) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("name", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "material not found!")));
        }
        return MaterialResponse.responseFromMaterial(material);
    }

    @Override
    public MaterialResponse update(Long id, MaterialRequest mr, String username){
        Material material = getAndCheckMaterial(id, username);
        material.setName(mr.getName());
        material.setLink(mr.getLink());
        return MaterialResponse.responseFromMaterial(materialRepository.save(material));
    }

    @Override
    public void deleteMaterial(Long id, String username) {
        Material material = getAndCheckMaterial(id, username);
        materialRepository.delete(material);
    }

    private Material getAndCheckMaterial(Long id, String username) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RestConflictException(Collections.singletonList(
                        new FieldErrorModel("id", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "material not found"))));
        if(!material.getCreator().equals(username)) {
            throw new RestUnauthorizedException(Collections.singletonList(
                    new FieldErrorModel("login", HttpStatus.CONFLICT.getReasonPhrase(),
                            "you cannot delete someone else's material")));
        }
        return material;
    }

    private List<MaterialResponse> materialToResponse(List<Material> materials){
        return materials.stream()
                .map(mat -> new MaterialResponse(mat.getId(), mat.getName(), mat.getCreator(), mat.getLink()))
                .collect(Collectors.toList());
    }
}
