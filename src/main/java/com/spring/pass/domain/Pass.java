package com.spring.pass.domain;

import com.spring.pass.domain.constant.PassStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Table(name = "pass") //TODO: Indexing
@Entity
public class Pass extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String packageId; //패키지 ID
    private String userId; //사용자 ID

    @Enumerated(EnumType.STRING)
    private PassStatus status; //이용권 상태
    private Integer remainingCount; //남은 횟수

    private LocalDateTime startedAt; //시작 일시
    private LocalDateTime endedAt; //종료 일시
    private LocalDateTime expiredAt; //만료 일시

    protected Pass() {
    }

    private Pass(String packageId, String userId, PassStatus status, Integer remainingCount, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime expiredAt) {
        this.packageId = packageId;
        this.userId = userId;
        this.status = status;
        this.remainingCount = remainingCount;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.expiredAt = expiredAt;
    }

    public static Pass of(String packageId, String userId, PassStatus status, Integer remainingCount, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime expiredAt) {
        return new Pass(packageId, userId, status, remainingCount, startedAt, endedAt, expiredAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pass that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}