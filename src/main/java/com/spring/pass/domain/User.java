package com.spring.pass.domain;

import com.spring.pass.domain.constant.UserStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Table(name = "user") //TODO: Indexing
@Entity
public class User extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName; //사용자 이름

    @Enumerated(EnumType.STRING)
    private UserStatus status; //사용자 상태
    private String phone; //폰 번호
    private String meta; //메타 정보(JSON)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
