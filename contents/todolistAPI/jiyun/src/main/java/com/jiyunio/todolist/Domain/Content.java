package com.jiyunio.todolist.Domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contentId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Member_id")
    private Member member;

    @Lob // 길이 제한 X
    @Column(name = "content")
    private String content;

    @Column(name = "checked")
    private boolean checked;

    @Column(name = "category")
    private String category;

    @Builder
    public Content(Member member, String content, boolean checked, String category) {
        this.member = member;
        this.content = content;
        this.checked = checked;
        this.category = category;
    }
}
