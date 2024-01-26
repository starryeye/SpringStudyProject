package dev.practice.multipledatasources.repository.memo;

import dev.practice.multipledatasources.repository.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
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
}
