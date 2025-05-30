package com.backend.app.dto;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {

    private Long id;
    private String name;
    private String description;
    private List<UserResponse> users;
}
// This class is used to transfer data for a group, including its ID, name, description, and a list of users in the group.
// It is used in the response of the GroupController to provide detailed information about a group.