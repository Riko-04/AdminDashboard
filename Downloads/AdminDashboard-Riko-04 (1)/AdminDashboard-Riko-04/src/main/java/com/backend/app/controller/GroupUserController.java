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
    // POST /group-users - Add a user to a group
    @PostMapping
    public ResponseEntity<GroupUserResponse> addUserToGroup(@RequestBody GroupUserResponse request) {
        // Assuming you have a method to convert GroupUser to GroupUserResponse
        var groupUser = groupUserService.createGroupUser(request);
        GroupUserResponse response = new GroupUserResponse(
            groupUser.getId(),
            groupUser.getGroupId(),
            groupUser.getUserId()
            // add other fields as needed
, null, null, null, null
        );
        return ResponseEntity.ok(response);
    }
    // DELETE /group-users/{groupId}/{userId} - Remove a user from a group
    @DeleteMapping("/{groupId}/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupUserService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }
}
