package com.wolox.training.controllers;

import com.wolox.training.Constant;
import com.wolox.training.exceptions.IdMismatchException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public List<Book> findAll() {
    return bookRepository.findAll();
  }

  @GetMapping("/{id}")
  public Book findById(@PathVariable Long id){
    return bookRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
  }

  @GetMapping(params = "title")
  public List<Book> findByTitle(@RequestParam String title) {
    return bookRepository.findByTitle(title).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    bookRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
    bookRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
    if (!book.getId().equals(id)) {
      throw new IdMismatchException(Constant.BOOK_ID_MISMATCH);
    }
    bookRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
    return bookRepository.save(book);
  }
}
