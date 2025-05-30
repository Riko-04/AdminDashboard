package com.backend.app.dto;

import java.util.List;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserResponse {
    private Long userId;
    private String groupName;
    private String username;
    private String email;
    private String role;

    private List<GroupSummary> groups;

    @Data
    @Builder
    public static class GroupSummary {
        private Long groupId;
        private String groupName;
        private String description;
    }
}
