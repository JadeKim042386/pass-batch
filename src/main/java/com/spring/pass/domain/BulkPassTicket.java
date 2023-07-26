package com.spring.pass.domain;

import com.spring.pass.domain.constant.BulkPassTicketStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(name = "bulk_pass_ticket")
@Entity
public class BulkPassTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long packageId; //패키지 ID
    private String userGroupId; //사용자 그룹 ID

    @Enumerated(EnumType.STRING)
    private BulkPassTicketStatus status; //상태
    private Integer count; //남은 횟수

    private LocalDateTime startedAt; //시작 일시
    private LocalDateTime endedAt; //종료 일시

    protected BulkPassTicket() {
    }

    private BulkPassTicket(Long packageId, String userGroupId, BulkPassTicketStatus status, Integer count, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.packageId = packageId;
        this.userGroupId = userGroupId;
        this.status = status;
        this.count = count;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static BulkPassTicket of(Long packageId, String userGroupId, BulkPassTicketStatus status, Integer count, LocalDateTime startedAt, LocalDateTime endedAt) {
        return new BulkPassTicket(packageId, userGroupId, status, count, startedAt, endedAt);
    }

    public void changeStatus(BulkPassTicketStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BulkPassTicket that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
