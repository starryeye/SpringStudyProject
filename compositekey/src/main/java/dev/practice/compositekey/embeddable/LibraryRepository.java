package dev.practice.compositekey.embeddable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, LibraryId> {
}
