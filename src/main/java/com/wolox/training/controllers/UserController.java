package com.wolox.training.controllers;

import com.wolox.training.Constant;
import com.wolox.training.exceptions.IdMismatchException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

@Api(value = "Operations pertaining to users")
@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @ApiOperation(value = "Returns the list of all users", response = List.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping
  public Page<User> allUsers(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortOrder) {
    return userRepository
        .findAll(PageRequest.of(page, pageSize, Direction.fromString(sortOrder), sortBy));
  }

  @ApiOperation(value = "Given an id, the corresponding user is returned", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping("/{id}")
  public User findById(
      @ApiParam(value = "id of the book to be found", required = true) @PathVariable Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
  }

  @ApiOperation(value = "Given an user name, the corresponding user is returned", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping(params = "userName")
  public User findByUsername(
      @ApiParam(value = "username of the user to be found", required = true) @RequestParam String userName) {
    return userRepository.findFirstByUsername(userName)
        .orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
  }

  @ApiOperation(value = "Creates a user record in the database", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = Constant.CREATED_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User createUser(@ApiParam(value = "User object to be saved") @RequestBody User user) {
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    return userRepository.save(user);
  }

  @ApiOperation(value = "Update an user given it's id", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @PutMapping("/{id}")
  public User updateUser(@ApiParam(value = "User update object") @RequestBody User user,
      @ApiParam(value = "Id of the user to be updated") @PathVariable Long id) {
    if (!user.getId().equals(id)) {
      throw new IdMismatchException(Constant.USER_ID_MISMATCH);
    }
    userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    return userRepository.save(user);
  }

  @ApiOperation(value = "Add a book to a user", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @PutMapping("/{id}/books/{bookId}")
  public User addBook(
      @ApiParam(value = "Id of the user to whom the book is going to be added") @PathVariable Long id,
      @ApiParam(value = "Id of the book to be added") @PathVariable Long bookId) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));

    user.addBook(book);
    return userRepository.save(user);
  }

  @ApiOperation(value = "Delete a user")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    userRepository.deleteById(id);
  }

  @ApiOperation(value = "Delete a book from a user", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @DeleteMapping("/{id}/books/{bookId}")
  public User removeBook(
      @ApiParam(value = "Id of the user to whom the book is going to be removed") @PathVariable Long id,
      @ApiParam(value = "Id of the book to be removed") @PathVariable Long bookId) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NotFoundException(Constant.BOOK_NOT_FOUND));
    user.removeBook(book);
    return userRepository.save(user);
  }

  @ApiOperation(value = "Get logged user info", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Constant.OK_MESSAGE),
      @ApiResponse(code = 404, message = Constant.NOT_FOUND_MESSAGE),
      @ApiResponse(code = 401, message = Constant.NOT_AUTHORIZED_MESSAGE),
      @ApiResponse(code = 403, message = Constant.FORBIDDEN_MESSAGE)
  })
  @GetMapping("/current-user")
  public User currentUser(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    return userRepository.findFirstByUsername(principal.getName())
        .orElseThrow(() -> new NotFoundException(Constant.USER_NOT_FOUND));
  }
}
