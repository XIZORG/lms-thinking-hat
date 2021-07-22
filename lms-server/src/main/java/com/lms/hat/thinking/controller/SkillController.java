package com.lms.hat.thinking.controller;

import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.model.skill.SkillRequest;
import com.lms.hat.thinking.model.skill.SkillResponse;
import com.lms.hat.thinking.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill")
public class SkillController {
    private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> createSkill(@RequestBody SkillRequest skill){
        return ResponseEntity.ok(skillService.saveSkill(skill));
    }

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAllSkills(){
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/{name}")
    public ResponseEntity<SkillResponse> getByName(@PathVariable(name = "name") String name) {
        return ResponseEntity.ok(skillService.findByNAme(name));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SkillResponse> editSkill(@PathVariable(name = "id") Long id,
                                            @RequestBody SkillRequest skill){
        return ResponseEntity.ok(skillService.editSkill(id, skill.getSkillName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse> deleteSkill(@PathVariable(name = "id") Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(new DefaultResponse());
    }
}
