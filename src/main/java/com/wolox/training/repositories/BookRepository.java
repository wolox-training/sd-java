package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  @Query("SELECT b FROM Book b WHERE "
      + "(:publisher IS NULL OR publisher = :publisher) AND "
      + "(:year IS NULL OR year = :year) AND "
      + "(:genre IS NULL OR genre = :genre)")
  Page<Book> findByPublisherAndYearAndGenre(
      @Param("publisher") String publisher, @Param("year") String year,
      @Param("genre") String genre, Pageable pageable
  );

  Optional<Book> findFirstByAuthor(String author);

  Book findFirstByIsbn(String isbn);

  Optional<List<Book>> findByTitle(String title);
}
