package com.wolox.training.controllers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import com.wolox.training.support.factories.BookFactory;
import com.wolox.training.support.factories.UserFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)

public class UserControllerTests {

  @MockBean
  UserRepository mockUserRepository;

  @MockBean
  BookRepository mockBookRepository;

  @Autowired
  MockMvc mvc;

  private User user = new UserFactory().createUserWithoutBooks();
  List<User> oneUserList = Arrays.asList(user);
  private User secondUser = new UserFactory().createUserWithoutBooks();
  private List<User> allUsers = Arrays.asList(user, secondUser);
  private Book book = new BookFactory().createBookWithOutUser();

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindAll_thenAllBooksAreReturned() throws Exception {
    when(mockUserRepository.findAll()).thenReturn(allUsers);
    mvc
        .perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username").value(user.getUsername()))
        .andExpect(jsonPath("$[1].username").value(secondUser.getUsername()));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindByIdWhichExists_thenUserIsReturned() throws Exception {
    when(mockUserRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
    mvc
        .perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            String.format(
                "{\"id\":null,\"username\":\"%s\","
                    + "\"name\":\"%s\",\"birthDate\":\"%s\",\"books\":[]}",
                user.getUsername(), user.getName(), user.getBirthDate()
            )
        ));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindByUserNameWhichExists_thenBookIsReturned() throws Exception {
    when(mockUserRepository.findFirstByUsername(user.getUsername())).thenReturn(
        java.util.Optional.ofNullable(user));
    mvc
        .perform(get("/api/users?userName=" + user.getUsername())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            String.format(
                "{\"id\":null,\"username\":\"%s\","
                    + "\"name\":\"%s\",\"birthDate\":\"%s\",\"books\":[]}",
                user.getUsername(), user.getName(), user.getBirthDate()
            )
        ));
  }

  @Test
  public void whenCreateUser_thenUserIsCreated() throws Exception {
    mvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \"sebastian\", \"username\": \"sebas\", "
            + "\"birthDate\": \"1997-03-11\", \"password\": \"hola123\"}"))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenCreateUserWithMissingRequiredParam_thenNoBookIsCreated() throws Exception {
    String url = ("/api/books");
    mvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \", \"username\": \"sebas\", "
            + "\"birthDate\": \"1997-03-11\"}"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenDeleteUserById_thenUserIsDeleted() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(user));
    mvc.perform(delete("/api/users/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(mockUserRepository, times(1)).deleteById(1L);
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenDeleteUserNotFound_thenThrowError() throws Exception {
    mvc.perform(delete("/api/users/-1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(mockUserRepository, times(0)).deleteById(-1L);
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenUpdateUser_thenUserIsUpdated() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(user));

    mvc.perform(put("/api/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\":\"1\", \"userName\":\"sebas\","
            + "\"name\":\"Sebastian\",\"birthDate\":\"1997-03-11\"}"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenUpdateUserIdMismatches_thenUserIsNotUpdated() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(user));

    mvc.perform(put("/api/users/-1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\":\"1\", \"userName\":\"sebas\","
            + "\"name\":\"Sebastian\",\"birthDate\":\"1997-03-11\"}"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenAddingANewBook_thenBookIsAddedToUser() throws Exception {

    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(book));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    mvc.perform(put("/api/users/1/books/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books", hasSize(1)))
        .andExpect(jsonPath("$.books[0].title").value(book.getTitle()));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenAddingANotExistentBook_thenBookIsNotAddedToUser() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    mvc.perform(put("/api/users/1/books/-1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenAddingBookToNonExistentUser_thenBookIsNotAddedToUser() throws Exception {
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(book));

    mvc.perform(put("/api/users/-1/books/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenAddingABookOwnedByUser_theBookIsNotAddedToUser() throws Exception {
    user.addBook(book);
    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(book));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    mvc.perform(put("/api/users/1/books/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenRemovingABook_thenBookIsRemovedFromUser() throws Exception {
    user.addBook(book);
    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(book));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    mvc.perform(delete("/api/users/1/books/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books", hasSize(0)));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenRemovingNotExistentBook_thenBookIsNotRemovedFromUser() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    mvc.perform(delete("/api/users/1/books/-1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenRemovingBookFromNonExistentUser_thenBookIsNotRemovedFromUser() throws Exception {
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(book));

    mvc.perform(delete("/api/users/-1/books/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }
}
