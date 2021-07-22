package com.lms.hat.thinking.service;

import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.exception.status.RestUnauthorizedException;
import com.lms.hat.thinking.model.material.Material;
import com.lms.hat.thinking.model.material.MaterialRequest;
import com.lms.hat.thinking.model.material.MaterialResponse;
import com.lms.hat.thinking.repository.*;
import com.lms.hat.thinking.service.impl.MaterialServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class MaterialServiceTest {

    private MaterialRepository materialRepository;
    private MaterialService materialService;

    @BeforeEach
    void setUp() {
        materialRepository = mock(MaterialRepository.class);
        materialService = new MaterialServiceImpl(materialRepository);
    }

    @Test
    void testSaveMaterial(){
        String newName = "test2";
        String existName = "test1";
        String username = "creator";

        MaterialRequest newMaterialRequest = new MaterialRequest();
        newMaterialRequest.setLink("bla-bla");
        newMaterialRequest.setName(newName);

        Material material = new Material();
        material.setCreator(username);
        material.setName(newName);
        material.setLink(newMaterialRequest.getLink());

        MaterialRequest existMaterial = new MaterialRequest();
        existMaterial.setLink("bla-bla");
        existMaterial.setName(existName);

        when(materialRepository.findByName(newName)).thenReturn(null);
        when(materialRepository.findByName(existName)).thenReturn(new Material());
        when(materialRepository.save(material)).thenReturn(material);

        MaterialResponse mr = materialService.save(newMaterialRequest, username);
        assertNotNull(mr);
        assertThat(mr.getCreator().equals(username));
        assertThat(mr.getLink().equals(newMaterialRequest.getLink()));
        assertThat(mr.getName().equals(newMaterialRequest.getName()));

        assertThrows(RestConflictException.class, () -> materialService.save(existMaterial, username));
    }

    @Test
    void testFindAllMaterials(){
        Material m1 = new Material();
        m1.setName("m1");

        Material m2 = new Material();
        m2.setName("m2");
        List<Material> materialList = List.of(m1, m2);
        when(materialRepository.findAll()).thenReturn(materialList);

        List<MaterialResponse> responses = materialService.findAll();
        assertNotNull(responses);
        assertThat(responses.get(0).getName().equals(m1.getName()));
        assertThat(responses.get(1).getName().equals(m2.getName()));
    }

    @Test
    void testFindMaterialByName(){
        String presentName = "mat1";
        String unPresentName = "mat2";

        Material material = new Material();
        material.setName(presentName);
        material.setLink("link");

        when(materialRepository.findByName(presentName)).thenReturn(material);
        when(materialRepository.findByName(unPresentName)).thenReturn(null);

        MaterialResponse materialResponse = materialService.findByName(presentName);
        assertNotNull(materialResponse);
        assertThat(materialResponse.getName().equals(presentName));
        assertThat(materialResponse.getLink().equals(material.getLink()));

        assertThrows(RestConflictException.class, () -> materialService.findByName(unPresentName));
    }

    @Test
    void testUpdateMaterial(){
        Long presentId = 1L;
        Long unPresentId = 2L;

        String presentName = "test2";
        String username = "creator";
        String notExistCreator = "badBoy";

        MaterialRequest newMaterialRequest = new MaterialRequest();
        newMaterialRequest.setLink("bla-bla");
        newMaterialRequest.setName(presentName);

        Material material = new Material();
        material.setCreator(username);
        material.setName(presentName);
        material.setLink("1 link");

        when(materialRepository.findById(presentId)).thenReturn(Optional.of(material));
        when(materialRepository.findById(unPresentId)).thenReturn(Optional.empty());

        when(materialRepository.save(material)).thenReturn(material);

        MaterialResponse materialResponse = materialService.update(presentId, newMaterialRequest, username);

        assertNotNull(materialResponse);
        assertThat(!materialResponse.getLink().equals(material.getLink()));
        assertThat(materialResponse.getName().equals(material.getName()));

        assertThrows(RestConflictException.class,() -> materialService.update(unPresentId, newMaterialRequest, username));
        assertThrows(RestUnauthorizedException.class,() -> materialService.update(presentId, newMaterialRequest, notExistCreator));
    }

    @Test
    void testDeleteMaterial(){
        Long presentId = 1L;
        Long unPresentId = 2L;

        String presentName = "test2";
        String username = "creator";
        String notExistCreator = "badBoy";

        Material material = new Material();
        material.setCreator(username);
        material.setName(presentName);
        material.setLink("1 link");

        when(materialRepository.findById(presentId)).thenReturn(Optional.of(material));
        when(materialRepository.findById(unPresentId)).thenReturn(Optional.empty());

        materialService.deleteMaterial(presentId, username);
        verify(materialRepository).findById(presentId);
        verify(materialRepository).delete(material);

        assertThrows(RestConflictException.class,() -> materialService.deleteMaterial(unPresentId, username));
        assertThrows(RestUnauthorizedException.class,() -> materialService.deleteMaterial(presentId, notExistCreator));
    }

}
