package com.spring.pass.repository;

import com.spring.pass.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    /**
     * WHERE UserGroup.userGroupId = :UserGroup.userGroupId
     * @param userGroupId
     */
    List<UserAccount> findByUserGroup_UserGroupId(String userGroupId);
}
