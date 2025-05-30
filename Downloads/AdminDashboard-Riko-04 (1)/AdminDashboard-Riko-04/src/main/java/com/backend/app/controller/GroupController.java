package com.backend.app.controller;

import com.backend.app.dto.GroupRequest;
import com.backend.app.dto.GroupResponse;
import com.backend.app.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "http://127.0.0.1:4200", allowCredentials = "true")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public List<GroupResponse> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public GroupResponse getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createGroup(@RequestBody GroupRequest request) {
        return groupService.createGroup(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateGroup(@PathVariable Long id, @RequestBody GroupRequest request) {
        return groupService.updateGroup(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteGroup(@PathVariable Long id) {
        return groupService.deleteGroup(id);
    }

    @PostMapping("/{groupId}/users/{userId}")
    public GroupResponse addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupService.addUserToGroup(groupId, userId);
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public GroupResponse removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupService.removeUserFromGroup(groupId, userId);
    }
}
