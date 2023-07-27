package com.spring.pass.repository;

import com.spring.pass.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, String> {
}
