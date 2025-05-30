package com.backend.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {

    @NotBlank(message = "Group name is required")
    private String name;

    private String description;
}
// This class is used to transfer data for creating or updating a group.
// It includes validation annotations to ensure that the group name is provided.