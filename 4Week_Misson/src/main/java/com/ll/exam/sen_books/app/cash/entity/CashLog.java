package com.ll.exam.sen_books.app.cash.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CashLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private long price; // 변동 금액
    private String eventType; // 변동 사유

    public CashLog(long id) {
        super(id);
    }
}
