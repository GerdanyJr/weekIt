package com.github.gerdanyJr.weekit.model.req;

import com.github.gerdanyJr.weekit.model.enums.AcademicRole;

import jakarta.validation.constraints.NotBlank;

public record CreateParticipationReq(
        @NotBlank AcademicRole role,
        @NotBlank Long studentId,
        @NotBlank Long courseId) {

}
