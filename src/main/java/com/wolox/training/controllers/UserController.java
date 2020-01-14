package com.wolox.training.controllers;

import com.wolox.training.Constant;
import com.wolox.training.exceptions.IdMismatchException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public List<User> allBooks() {
    return userRepository.findAll();
  }

  @GetMapping("/{id}")
  public User findById(@PathVariable Long id){
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
  }

  @GetMapping(params = "userName")
  public User findByUsername(@RequestParam String userName){
    return userRepository.findFirstByUserName(userName).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User createUser(@RequestBody User user){
    return userRepository.save(user);
  }

  @PutMapping("/{id}")
  public User updateUser(@RequestBody User user, @PathVariable Long id){
    if (!user.getId().equals(id)) {
      throw new IdMismatchException(Constant.USER_ID_MISMATCH);
    }
    userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    return userRepository.save(user);
  }

  @PutMapping("/{id}/books/{book_id}")
  public User addBook(@PathVariable Long id, @PathVariable  Long bookId){
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
    user.addBook(book);
    return userRepository.save(user);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id){
    userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    userRepository.deleteById(id);
  }

  @DeleteMapping("/{id}/books/{book_id}")
  public User removeBook(@PathVariable Long id, @PathVariable Long bookId){
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
    user.removeBook(book);
    return userRepository.save(user);
  }
}
