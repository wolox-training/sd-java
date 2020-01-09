package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
  Book findFirstByAuthor(String author);
  List findByTitle(String title);
}
