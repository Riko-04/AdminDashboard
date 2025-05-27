package com.backend.app.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserResponse {
    private Long id;
    private Long groupId;
    private String groupName;
    private String username;
    private String email;
    private String role;
    private String profileImage;
}
