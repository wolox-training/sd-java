package com.wolox.training.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wolox.training.Constant;
import com.wolox.training.exceptions.IdMismatchException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.filters.BookFilters;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import com.wolox.training.services.OpenLibraryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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

@Api(value = "Operations pertaining to books")
@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private OpenLibraryService openLibraryService;

  @Autowired
  private UserRepository userRepository;

  @ApiOperation(value = "Returns the list of all books", response = List.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping
  public Page<Book> findAll(
      BookFilters filters,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortOrder) {
    PageRequest pageRequest = PageRequest
        .of(page, pageSize, Direction.fromString(sortOrder), sortBy);
    return bookRepository
        .findByPublisherAndYearAndGenre(
            filters.getPublisher(), filters.getYear(), filters.getGenre(), pageRequest
        );
  }

  @ApiOperation(value = "Given an id, the corresponding book is returned", response = Book.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping("/{id}")
  public Book findById(
      @ApiParam(value = "id of the book to be found", required = true) @PathVariable Long id) {
    return bookRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
  }

  @ApiOperation(value = "Given a title, the corresponding book is returned", response = Book.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping(params = "title")
  public List<Book> findByTitle(
      @ApiParam(value = "title of the book to be found", required = true) @RequestParam String title) {
    return bookRepository.findByTitle(title)
        .orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
  }

  @ApiOperation(value = "Creates a Book record in the database", response = Book.class)
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = Constant.CREATED_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(
      @ApiParam(value = "Book object store in database table", required = true) @RequestBody Book book) {
    return bookRepository.save(book);
  }

  @ApiOperation(value = "Deletes a book record given it's id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @DeleteMapping("/{id}")
  public void delete(
      @ApiParam(value = "Book id of the book to be deleted from the database", required = true) @PathVariable Long id) {
    bookRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
    bookRepository.deleteById(id);
  }

  @ApiOperation(value = "Updates a book record given it's id", response = Book.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @PutMapping("/{id}")
  public Book updateBook(
      @ApiParam(value = "Updated book object", required = true) @RequestBody Book book,
      @ApiParam(value = "Id of the book to be updated") @PathVariable Long id) {
    if (!book.getId().equals(id)) {
      throw new IdMismatchException(Constant.BOOK_ID_MISMATCH);
    }
    bookRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
    return bookRepository.save(book);
  }

  @ApiOperation(value = "Given an ISBN, the corresponding book is returned", response = Book.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping("/isbn/{ISBN}")
  public Book findByISBN(
      @ApiParam(value = "ISBN of the book to be found", required = true) @PathVariable String ISBN)
      throws JsonProcessingException {
    return openLibraryService.bookInfo(ISBN);
  }
}
