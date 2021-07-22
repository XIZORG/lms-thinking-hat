package com.lms.hat.thinking.controller;

import com.lms.hat.thinking.config.security.services.CustomUserDetails;
import com.lms.hat.thinking.model.material.MaterialRequest;
import com.lms.hat.thinking.model.material.MaterialResponse;
import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired
    public MaterialController( MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<MaterialResponse> createNewMaterial(@Valid @RequestBody MaterialRequest materialRequest,
                                                    Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        MaterialResponse mr = materialService.save(materialRequest, cud.getUsername());
        return ResponseEntity.ok(mr);
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponse>> getAll() {
        return ResponseEntity.ok(materialService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<MaterialResponse> getByName(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok(materialService.findByName(name));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MaterialResponse> update(@PathVariable(value = "id") Long id,
                                            @Valid @RequestBody MaterialRequest materialRequest,
                                         Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return ResponseEntity.ok(materialService.update(id, materialRequest,cud.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse> deleteMaterial(@PathVariable(value = "id") Long id,
                                                 Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        materialService.deleteMaterial(id, cud.getUsername());
        return ResponseEntity.ok(new DefaultResponse());
    }
}
