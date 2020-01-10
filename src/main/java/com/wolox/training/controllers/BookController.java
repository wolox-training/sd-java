package com.wolox.training.controllers;

import com.wolox.training.exceptions.books.BookIdMismatchException;
import com.wolox.training.exceptions.books.BookNotFoundException;
import com.wolox.training.models.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public Iterable findAll() {
    return bookRepository.findAll();
  }

  @GetMapping(params = "title")
  public Optional<List<Book>> findByTitle(@RequestParam String title) {
    return bookRepository.findByTitle(title);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    if (!bookRepository.findById(id).isPresent()) {
      throw new BookNotFoundException("Book Not Found");
    }
    bookRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
    if (!book.getId().equals(id)) {
      throw new BookIdMismatchException("Book Id Mismatch");
    }
    if (!bookRepository.findById(id).isPresent()) {
      throw new BookNotFoundException("Book Not Found");
    }
    return bookRepository.save(book);
  }
}
