package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
  Optional<Book> findFirstByAuthor(String author);
  Optional<List<Book>> findByTitle(String title);
}
