package com.backend.app.controller;

import com.backend.app.dto.GroupResponse;
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

    // Get all users in a specific group
    @GetMapping("/{groupId}")
    public ResponseEntity<List<GroupUserResponse>> getUsersInGroup(@PathVariable Long groupId) {
        List<GroupUserResponse> users = groupUserService.getUsersInGroup(groupId);
        return ResponseEntity.ok(users);
    }

    // Get all groups for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<GroupUserResponse> getUserWithGroups(@PathVariable Long userId) {
        GroupUserResponse userGroups = groupUserService.getUserWithGroups(userId);
        return ResponseEntity.ok(userGroups);
    }

    // Add a user to a group
    @PostMapping("/{groupId}/add/{userId}")
    public ResponseEntity<GroupResponse> addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupUserService.addUserToGroup(groupId, userId));
    }

    // Remove a user from a group
    @DeleteMapping("/{groupId}/remove/{userId}")
    public ResponseEntity<GroupResponse> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupUserService.removeUserFromGroup(groupId, userId));
    }
}
