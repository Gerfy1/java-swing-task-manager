package com.task.taskManagerSolid.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskDTO(

        Long id,

        @NotBlank(message = "O título não pode ser vazio!") String title,
        String description,
        boolean completed
) {
}
