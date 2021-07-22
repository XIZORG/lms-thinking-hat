package com.lms.hat.thinking.model.material;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class MaterialToCourseRequest {
    private List<Long> materialId;
}
