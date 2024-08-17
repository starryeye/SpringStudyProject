package dev.starryeye.globaltransaction.domain;

import dev.starryeye.globaltransaction.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString
@Getter
@Entity
@Table(name = "my_memo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = AuditingEntityListener.class)
public class MemoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Builder
    private MemoEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static MemoEntity create(String title, String content) {
        return MemoEntity.builder()
                .title(title)
                .content(content)
                .build();
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
