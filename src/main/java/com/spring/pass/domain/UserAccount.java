package com.spring.pass.domain;

import com.spring.pass.domain.constant.UserStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Table(name = "user_account") //TODO: Indexing
@Entity
public class UserAccount extends AuditingFields {
    @Id
    private String userId;
    private String userName; //사용자 이름

    @Enumerated(EnumType.STRING)
    private UserStatus status; //사용자 상태
    private String phone; //폰 번호
    private String meta; //메타 정보(JSON)

    protected UserAccount() {
    }

    private UserAccount(String userId, String userName, UserStatus status, String phone, String meta) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.phone = phone;
        this.meta = meta;
    }

    public static UserAccount of(String userId, String userName, UserStatus status, String phone, String meta) {
        return new UserAccount(userId, userName, status, phone, meta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return this.getUserId() != null && Objects.equals(this.getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}
