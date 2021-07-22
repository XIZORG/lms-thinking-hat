package com.lms.hat.thinking.model.material;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MaterialRequest {
    @NotBlank(message = "https://www.google.com/")
    private String link;

    @NotBlank(message = "Gidronez")
    private String name;

}
