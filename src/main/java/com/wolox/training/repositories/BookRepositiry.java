package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepositiry extends JpaRepository<Book, String> {
  Book findFirstByAuthor(String author);
}
