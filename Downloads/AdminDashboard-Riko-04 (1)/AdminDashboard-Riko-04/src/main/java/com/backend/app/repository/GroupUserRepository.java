package com.backend.app.repository;

import com.backend.app.entity.GroupUser;
import com.backend.app.entity.Group;
import com.backend.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    List<GroupUser> findByGroup(Group group);
    boolean existsByGroupAndUser(Group group, User user);
}
