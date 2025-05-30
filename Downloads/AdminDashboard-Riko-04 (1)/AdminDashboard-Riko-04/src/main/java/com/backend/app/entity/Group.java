package com.backend.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`groups`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("group-groupUser")
    private Set<GroupUser> groupUsers = new HashSet<>();

    public void addGroupUser(GroupUser groupUser) {
        this.groupUsers.add(groupUser);
        groupUser.setGroup(this);
    }

    public void removeGroupUser(GroupUser groupUser) {
        this.groupUsers.remove(groupUser);
        groupUser.setGroup(null);
    }
}
