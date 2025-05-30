package com.backend.app.service;

import com.backend.app.dto.GroupUserRequest;
import com.backend.app.dto.GroupUserResponse;
import com.backend.app.entity.Group;
import com.backend.app.entity.GroupUser;
import com.backend.app.entity.User;
import com.backend.app.repository.GroupRepository;
import com.backend.app.repository.GroupUserRepository;
import com.backend.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupUserService {

    private final GroupUserRepository groupUserRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupUserService(GroupUserRepository groupUserRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.groupUserRepository = groupUserRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public GroupUser createGroupUser(GroupUserResponse request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (groupUserRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("User is already a member of the group");
        }

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(user)
                .build();

        return groupUserRepository.save(groupUser);
    }

    public List<GroupUserResponse> getUsersInGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return groupUserRepository.findByGroup(group).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public GroupUserResponse addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (groupUserRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("User is already a member of the group");
        }

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(user)
                .build();

        GroupUser savedGroupUser = groupUserRepository.save(groupUser);
        return mapToResponse(savedGroupUser);
    }

    public void removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupUser> groupUsers = groupUserRepository.findByGroup(group);
        groupUsers.stream()
                .filter(gu -> gu.getUser().getId().equals(userId))
                .findFirst()
                .ifPresent(groupUserRepository::delete);
    }

    private GroupUserResponse mapToResponse(GroupUser groupUser) {
        return GroupUserResponse.builder()
                .id(groupUser.getId())
                .groupId(groupUser.getGroup().getId())
                .groupName(groupUser.getGroup().getName())
                .username(groupUser.getUser().getUsername())
                .email(groupUser.getUser().getEmail())
                .role(groupUser.getUser().getRole())
                .profileImage(groupUser.getUser().getProfileImage())
                .build();
    }
}
