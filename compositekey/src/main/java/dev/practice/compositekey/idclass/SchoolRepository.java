package dev.practice.compositekey.idclass;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, SchoolId> {
}
