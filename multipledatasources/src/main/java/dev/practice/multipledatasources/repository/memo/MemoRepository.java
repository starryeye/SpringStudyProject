package dev.practice.multipledatasources.repository.memo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<MemoEntity, Long> {

    List<MemoEntity> findByTitle(String title);
}
