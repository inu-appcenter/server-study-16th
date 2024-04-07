package com.jiyunio.todolist.Domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dateId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Content_id")
    private Content content;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "writeDate")
    private java.util.Date writeDate;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "setDate")
    private java.util.Date setDate;

    @Builder
    protected Date(Content content, java.util.Date writeDate, java.util.Date setDate) {
        this.content = content;
        this.writeDate = writeDate;
        this.setDate = setDate;
    }

}
