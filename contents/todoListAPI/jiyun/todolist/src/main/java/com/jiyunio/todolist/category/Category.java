package com.jiyunio.todolist.category;

import com.jiyunio.todolist.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    private String category;

    @Builder
    protected Category(Member member, String category) {
        this.member = member;
        this.category = category;
    }

    protected void updateCategory(String category) {
        this.category = category;
    }
}
