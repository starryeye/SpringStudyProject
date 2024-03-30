package dev.practice.multipledatasources.repository.todo;

import dev.practice.multipledatasources.repository.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "my_todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = AuditingEntityListener.class)
public class TodoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Boolean completed;

    @Builder
    private TodoEntity(String title, Boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public static TodoEntity create(String title) {
        return TodoEntity.builder()
                .title(title)
                .completed(Boolean.FALSE)
                .build();
    }

    public void changeCompleted(Boolean completed) {
        this.completed = completed;
    }
}
