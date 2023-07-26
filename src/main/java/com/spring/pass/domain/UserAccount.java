package com.spring.pass.domain;

import com.spring.pass.domain.constant.UserAccountStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//TODO: save 처리시 불필요한 select 방지를 위한 isNew 추가
@ToString(callSuper = true)
@Getter
@Table(name = "user_account") //TODO: Indexing
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class UserAccount extends AuditingFields {
    @Id
    private String userId;
    private String userName; //사용자 이름

    @ManyToOne(optional = false)
    @JoinColumn(name = "userGroupId")
    private UserGroup userGroup; //속한 사용자 그룹

    @Enumerated(EnumType.STRING)
    private UserAccountStatus status; //사용자 상태
    private String phone; //폰 번호

    //json 형태로 저장되어 있는 문자열 데이터를 Map으로 매핑
    @Type(type="json")
    @Column(columnDefinition = "json")
    private Map<String, Object> meta = new HashMap<>(); //메타 정보(JSON)

    protected UserAccount() {
    }

    private UserAccount(String userId, String userName, UserGroup userGroup, UserAccountStatus status, String phone, Map<String, Object> meta) {
        this.userId = userId;
        this.userGroup = userGroup;
        this.userName = userName;
        this.status = status;
        this.phone = phone;
        this.meta = meta;
    }

    public static UserAccount of(String userId, String userName, UserGroup userGroup, UserAccountStatus status, String phone, Map<String, Object> meta) {
        return new UserAccount(userId, userName, userGroup, status, phone, meta);
    }

    public String getUuid() {
        String uuid = null;
        if (meta.containsKey("uuid")) {
            uuid = String.valueOf(meta.get("uuid"));
        }
        return uuid;
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
