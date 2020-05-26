package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findFirstByAuthor(String author);

  Book findFirstByIsbn(String isbn);

  Optional<List<Book>> findByTitle(String title);
}
