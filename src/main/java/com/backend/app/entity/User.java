package com.backend.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String role;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-groupUser")
    private Set<GroupUser> groupUsers = new HashSet<>();

    public void addGroupUser(GroupUser groupUser) {
        this.groupUsers.add(groupUser);
        groupUser.setUser(this);
    }

    public void removeGroupUser(GroupUser groupUser) {
        this.groupUsers.remove(groupUser);
        groupUser.setUser(null);
    }

    @JsonProperty("groups")
    public Set<Group> getGroups() {
        return groupUsers.stream()
                .map(GroupUser::getGroup)
                .collect(Collectors.toSet());
    }
}
