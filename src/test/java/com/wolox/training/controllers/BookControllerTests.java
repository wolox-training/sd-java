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
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import com.wolox.training.support.factories.BookFactory;
import java.util.Arrays;
import java.util.List;
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
@WebMvcTest(BookController.class)

public class BookControllerTests {

  @MockBean
  BookRepository mockBookRepository;
  @Autowired
  MockMvc mvc;
  @MockBean
  private UserRepository userRepository;
  private Book book = new BookFactory().createBookWithOutUser();
  private Book secondBook = new BookFactory().createBookWithOutUser();
  private List<Book> allBooks = Arrays.asList(book, secondBook);
  private List<Book> oneBookList = Arrays.asList(book);


  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindAll_thenAllBooksAreReturned() throws Exception {
    when(mockBookRepository.findAll()).thenReturn(allBooks);
    mvc
        .perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title").value(book.getTitle()))
        .andExpect(jsonPath("$[1].title").value(secondBook.getTitle()));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindByIdWhichExists_thenBookIsReturned() throws Exception {
    when(mockBookRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(book));
    mvc
        .perform(get("/api/books/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            String.format(
                "{\"id\":null,\"genre\":\"%s\","
                    + "\"author\":\"%s\",\"image\":\"%s\",\"title\":\""
                    + "%s\",\"publisher\":\"%s\",\"year\":"
                    + "\"%s\",\"pages\":%s,\"isbn\":\"%s\"}",
                book.getGenre(), book.getAuthor(), book.getImage(), book.getTitle(),
                book.getPublisher(), book.getYear(), book.getPages(), book.getIsbn()
            )
        ));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindByTitleWhichExists_thenBookIsReturned() throws Exception {
    when(mockBookRepository.findByTitle(book.getTitle())).thenReturn(
        java.util.Optional.ofNullable(oneBookList));
    mvc
        .perform(get("/api/books?title=" + book.getTitle()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].title").value(book.getTitle()));
  }

  @Test
  public void whenCreateBook_thenBookIsCreated() throws Exception {
    mvc.perform(post("/api/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"genre\":\"Some Genre\","
            + "\"author\":\"Some Author\",\"image\":\"Some image\",\"title\":\""
            + "Some title\",\"publisher\":\"Some publisher\",\"year\":"
            + "\"2020\",\"pages\":999,\"isbn\":\"8823193\"}"))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenCreateBookWithMissingRequiredParam_thenNoBookIsCreated() throws Exception {
    String url = ("/api/books");
    mvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"genre\":\"Some Genre\","
            + "\"author\":\"Some Author\",\"image\":\"Some image\",\"title\":\""
            + "Some title\",\"publisher\":\"Some publisher\",\"year\":"
            + "\"2020\",\"pages\":999,\"isbn\":\"}"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenDeleteBookById_thenBookIsDeleted() throws Exception {
    when(mockBookRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(book));
    mvc.perform(delete("/api/books/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(mockBookRepository, times(1)).deleteById(1L);
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenDeleteBookNotFound_thenThrowError() throws Exception {
    mvc.perform(delete("/api/books/-1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(mockBookRepository, times(0)).deleteById(-1L);
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenUpdateBook_thenBookIsUpdated() throws Exception {
    when(mockBookRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(book));

    mvc.perform(put("/api/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\":\"1\", \"genre\":\"Some Genre\","
            + "\"author\":\"Some Author\",\"image\":\"Some image\",\"title\":\""
            + "Some title\",\"publisher\":\"Some publisher\",\"year\":"
            + "\"2020\",\"pages\":999,\"isbn\":999 }"))
        .andExpect(status().isOk());
  }
}
