package com.backend.app.controller;

import com.backend.app.dto.GroupUserResponse;
import com.backend.app.service.GroupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group-users")
@RequiredArgsConstructor
public class GroupUserController {

    private final GroupUserService groupUserService;

    // GET /group-users/{groupId} - Get all users in a specific group
    @GetMapping("/{groupId}")
    public ResponseEntity<List<GroupUserResponse>> getUsersInGroup(@PathVariable Long groupId) {
        List<GroupUserResponse> users = groupUserService.getUsersInGroup(groupId);
        return ResponseEntity.ok(users);
    }
}
