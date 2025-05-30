package com.backend.app.service;

import com.backend.app.dto.GroupResponse;
import com.backend.app.dto.GroupUserRequest;
import com.backend.app.dto.GroupUserResponse;
import com.backend.app.dto.UserResponse;
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

    public GroupUser createGroupUser(GroupUserRequest request) {
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

    public GroupResponse addUserToGroup(Long groupId, Long userId) {
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

        groupUserRepository.save(groupUser);

        return mapToGroupResponse(group);
    }

    public GroupUserResponse getUserWithGroups(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GroupUser> groupUsers = groupUserRepository.findByUser(user);

        List<GroupUserResponse.GroupSummary> groups = groupUsers.stream()
                .map(gu -> GroupUserResponse.GroupSummary.builder()
                        .groupId(gu.getGroup().getId()) 
                        .groupName(gu.getGroup().getName())
                        .description(gu.getGroup().getDescription())
                        .build())
                .collect(Collectors.toList());

        return GroupUserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .groups(groups)
                .build();
        }


    public GroupResponse removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupUser> groupUsers = groupUserRepository.findByGroup(group);
        groupUsers.stream()
                .filter(gu -> gu.getUser().getId().equals(userId))
                .findFirst()
                .ifPresent(groupUserRepository::delete);

        return mapToGroupResponse(group);
    }

    private GroupUserResponse mapToResponse(GroupUser groupUser) {
        GroupUserResponse.GroupSummary groupSummary = GroupUserResponse.GroupSummary.builder()
                .groupId(groupUser.getGroup().getId())
                .groupName(groupUser.getGroup().getName())
                .description(groupUser.getGroup().getDescription())
                .build();
        User user = groupUser.getUser();

        return GroupUserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .groups(List.of(groupSummary))
                .build();
        }

    private GroupResponse mapToGroupResponse(Group group) {
        List<UserResponse> users = groupUserRepository.findByGroup(group).stream()
                .map(groupUser -> {
                    User user = groupUser.getUser();
                    return new UserResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRole()
                    );
                }).collect(Collectors.toList());

        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                users
        );
    }
}
