package com.spring.pass.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Table(name = "package") //TODO: Indexing
@Entity
public class Package extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String packageName; //패키지 이름
    private Integer count; //횟수
    private Integer period; //기간

    protected Package() {
    }

    private Package(String packageName, Integer count, Integer period) {
        this.packageName = packageName;
        this.count = count;
        this.period = period;
    }

    public static Package of(String packageName, Integer count, Integer period) {
        return new Package(packageName, count, period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Package that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
