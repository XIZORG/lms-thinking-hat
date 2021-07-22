package com.lms.hat.thinking.model.material;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaterialResponse {
    private Long id;
    private String name;
    private String creator;
    private String link;

    public static MaterialResponse responseFromMaterial(Material material) {
        return new MaterialResponse(material.getId(),material.getName(),material.getCreator(),material.getLink());
    }
}
