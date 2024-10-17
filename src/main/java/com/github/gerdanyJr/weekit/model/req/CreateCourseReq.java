package com.github.gerdanyJr.weekit.model.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCourseReq(
        @NotBlank(message = "name is required") 
        @Size(min = 3, message = "name must contain at least 3 characters") 
        @Size(max = 50, message = "name must not contain more than 50 characters") String name,
        @NotBlank(message = "description is required") 
        @Size(min = 3, message = "description must contain at least 3 characters")
        @Size(max = 255, message = "name must not contain more than 255 characters")
        String description) {

}
