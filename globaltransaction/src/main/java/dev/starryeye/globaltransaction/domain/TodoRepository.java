package dev.starryeye.globaltransaction.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    List<TodoEntity> findByTitle(String title);
}
