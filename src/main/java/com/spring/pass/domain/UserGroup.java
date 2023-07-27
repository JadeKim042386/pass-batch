package com.spring.pass.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(name = "user_group")
@Entity
public class UserGroup extends AuditingFields {
    @Id
    private String userGroupId;

    private String userGroupName;
    private String description;

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL)
    private Set<UserAccount> userAccount = new HashSet<>();

    protected UserGroup() {
    }

    private UserGroup(String userGroupId, String userGroupName, String description) {
        this.userGroupId = userGroupId;
        this.userGroupName = userGroupName;
        this.description = description;
    }

    public static UserGroup of(String userGroupId, String userGroupName, String description) {
        return new UserGroup(userGroupId, userGroupName, description);
    }

    public static UserGroup of(String userGroupId, String userGroupName) {
        return new UserGroup(userGroupId, userGroupName, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGroup that)) return false;
        return this.getUserGroupId() != null && Objects.equals(this.getUserGroupId(), that.getUserGroupId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserGroupId());
    }
}
