package com.wolox.training.models;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.wolox.training.repositories.BookRepository;
import com.wolox.training.support.factories.BookFactory;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)

public class BookTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BookRepository bookRepository;

  private Book        book;
  private BookFactory bookFactory = new BookFactory();

  @Before
  public void init() {
    book = bookFactory.createBookWithOutUser();
    entityManager.persist(book);
    entityManager.flush();
  }

  @After
  public void clean() {
    entityManager.clear();
    entityManager.flush();
  }

  @Test
  public void whenCreateBook_thenBookIsPersisted() {
    Book persistedBook = bookRepository.findById(book.getId()).orElse(null);

    assertThat(persistedBook.getGenre(), equalTo(book.getGenre()));
    assertThat(persistedBook.getAuthor(), equalTo(book.getAuthor()));
    assertThat(persistedBook.getTitle(), equalTo(book.getTitle()));
    assertThat(persistedBook.getIsbn(), equalTo(book.getIsbn()));
    assertThat(persistedBook.getSubtitle(), equalTo(book.getSubtitle()));
    assertThat(persistedBook.getImage(), equalTo(book.getImage()));
    assertThat(persistedBook.getYear(), equalTo(book.getYear()));
    assertThat(persistedBook.getPages(), equalTo(book.getPages()));
    assertThat(persistedBook.getPublisher(), equalTo(book.getPublisher()));
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutPages_thenThrowException() {
    book.setPages(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutIsbn_thenThrowException() {
    book.setIsbn(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutImage_thenThrowException() {
    book.setImage(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutPublisher_thenThrowException() {
    book.setPublisher(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutAuthor_thenThrowException() {
    book.setAuthor(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutTitle_thenThrowException() {
    book.setTitle(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutSubtitle_thenThrowException() {
    book.setSubtitle(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutYear_thenThrowException() {
    book.setYear(null);
    entityManager.persist(book);
    entityManager.flush();
  }

  @Test
  public void whenFindFirstByAuthor_thenReturnBook() {
    Book bookByAuthor = bookRepository.findFirstByAuthor(book.getAuthor()).orElse(null);
    assertThat(bookByAuthor.getAuthor(), equalTo(book.getAuthor()));
  }

  @Test
  public void whenFindFirstByAuthorWithUnknownAuthor_thenReturnNull() {
    Book bookByAuthor = bookRepository.findFirstByAuthor("n0t 4 n4m3").orElse(null);
    assertThat(bookByAuthor == null, is(true));
  }

  @Test
  public void whenFindByTitle_thenReturnListOfBooks() {
    Book second_book = bookFactory.createBookWithOutUser();
    entityManager.persist(second_book);
    entityManager.flush();
    List<Book> booksByTitle = bookRepository.findByTitle(second_book.getTitle()).orElse(null);
    assertThat(booksByTitle.get(0).getAuthor(), equalTo(second_book.getAuthor()));
  }

  @Test
  public void whenFindByTitle_thenReturnEmptyList() {
    Book bookByTitle = bookRepository.findFirstByAuthor("n0t 4 n4m3").orElse(null);
    assertThat(bookByTitle == null, is(true));
  }
}
