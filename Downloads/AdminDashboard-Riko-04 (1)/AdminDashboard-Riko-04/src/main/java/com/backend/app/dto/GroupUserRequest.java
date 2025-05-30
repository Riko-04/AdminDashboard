package com.backend.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserRequest {
    private Long groupId;
    private Long userId;
}
