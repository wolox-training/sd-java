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
import com.wolox.training.services.OpenLibraryService;
import com.wolox.training.support.factories.BookFactory;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
  @MockBean
  private OpenLibraryService mockOpenLibraryService;
  private Book book = new BookFactory().createBookWithOutUser();
  private Book secondBook = new BookFactory().createBookWithOutUser();
  private Page<Book> allBooksPaginated = new PageImpl(Arrays.asList(book, secondBook));
  private List<Book> oneBookList = Arrays.asList(book);

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindAll_thenAllBooksAreReturned() throws Exception {
    PageRequest pageRequest = PageRequest
        .of(0, 10, Direction.ASC, "id");
    when(mockBookRepository.findByPublisherAndYearAndGenre(null, null, null, pageRequest))
        .thenReturn(allBooksPaginated);
    mvc
        .perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].title").value(book.getTitle()))
        .andExpect(jsonPath("$.content[1].title").value(secondBook.getTitle()));
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

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  public void whenFindExternalBook_thenBookIsReturned() throws Exception {
    when(mockOpenLibraryService.bookInfo("123"))
        .thenReturn(book);

    mvc.perform(get("/api/books/isbn/123")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\":\"1\", \"genre\":\"Some Genre\","
            + "\"author\":\"Some Author\",\"image\":\"Some image\",\"title\":\""
            + "Some title\",\"publisher\":\"Some publisher\",\"year\":"
            + "\"2020\",\"pages\":999,\"isbn\":999 }"))
        .andExpect(status().isOk());
  }
}
