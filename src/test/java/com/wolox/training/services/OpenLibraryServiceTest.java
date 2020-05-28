package com.wolox.training.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.support.factories.BookFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
public class OpenLibraryServiceTest {

  @InjectMocks
  private OpenLibraryService openLibraryService;
  @Mock
  private BookRepository mockBookRepository;
  @Mock
  private RestTemplate restTemplate;

  @Test
  public void whenBookIsNotFound_ThenExceptionIsThrown() {
    String ISBN = "123";
    final String url =
        "https://openlibrary.org/api/books?bibkeys=ISBN:" + ISBN + "&format=json&jscmd=data";
    when(restTemplate.getForObject(url, String.class))
        .thenReturn("{}");

    Exception exception = assertThrows(NotFoundException.class, () -> {
      openLibraryService.bookInfo(ISBN);
    });

    String expectedMessage = "Book with ISBN: " + ISBN + "not found";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void whenBookIsFoundButAlreadyInDatabase_ThenBookIsReturnedButNotSavedAgain()
      throws JsonProcessingException {
    String ISBN = "0385472579";
    Book book = new BookFactory().createBookWithOutUser();
    book.setIsbn(ISBN);

    final String url =
        "https://openlibrary.org/api/books?bibkeys=ISBN:" + ISBN + "&format=json&jscmd=data";
    when(restTemplate.getForObject(url, String.class)).thenReturn(externalBookResponse());
    when(mockBookRepository.findFirstByIsbn(ISBN)).thenReturn(
        book);

    Book openLibraryBook = openLibraryService.bookInfo(ISBN);
    assertThat(book.getIsbn()).isSameAs(openLibraryBook.getIsbn());
    verify(mockBookRepository, times(0)).save(Mockito.any(Book.class));
  }

  @Test
  public void whenBookIsFoundAndNotInDatabase_ThenBookIsReturnedAndSaved()
      throws JsonProcessingException {
    String ISBN = "0385472579";
    Book book = new BookFactory().createBookWithOutUser();
    book.setIsbn(ISBN);

    final String url =
        "https://openlibrary.org/api/books?bibkeys=ISBN:" + ISBN + "&format=json&jscmd=data";
    when(restTemplate.getForObject(url, String.class)).thenReturn(externalBookResponse());
    when(mockBookRepository.findFirstByIsbn(ISBN)).thenReturn(null);
    when(mockBookRepository.save(Mockito.any(Book.class))).thenReturn(book);

    Book openLibraryBook = openLibraryService.bookInfo(ISBN);
    assertThat(openLibraryBook.getIsbn()).isSameAs(ISBN);
    verify(mockBookRepository, times(1)).save(Mockito.any(Book.class));
  }

  private String externalBookResponse() {
    return "{\"ISBN:0385472579\": {"
        + "\"publishers\": [{\"name\": \"Anchor Books\"}], "
        + "\"pagination\": \"159 p. :\", "
        + "\"identifiers\": {\"lccn\": [\"93005405\"], "
        + "\"openlibrary\": [\"OL1397864M\"], "
        + "\"isbn_10\": [\"0385472579\"], "
        + "\"librarything\": [\"192819\"], "
        + "\"goodreads\": [\"979250\"]}, "
        + "\"subtitle\": \"shouts of nothingness\", "
        + "\"title\": \"Zen speaks\", "
        + "\"url\": \"https://openlibrary.org/books/OL1397864M/Zen_speaks\", "
        + "\"number_of_pages\": 159, "
        + "\"cover\": {\"small\": \"https://covers.openlibrary.org/b/id/240726-S.jpg\", \"large\": \"https://covers.openlibrary.org/b/id/240726-L.jpg\", \"medium\": \"https://covers.openlibrary.org/b/id/240726-M.jpg\"}, "
        + "\"subjects\": [{\"url\": \"https://openlibrary.org/subjects/caricatures_and_cartoons\", \"name\": \"Caricatures and cartoons\"}, {\"url\": \"https://openlibrary.org/subjects/zen_buddhism\", \"name\": \"Zen Buddhism\"}], "
        + "\"publish_date\": \"1994\", "
        + "\"key\": \"/books/OL1397864M\", "
        + "\"authors\": [{\"url\": \"https://openlibrary.org/authors/OL223368A/Zhizhong_Cai\", \"name\": \"Zhizhong Cai\"}], "
        + "\"classifications\": {\"dewey_decimal_class\": [\"294.3/927\"], \"lc_classifications\": [\"BQ9265.6 .T7313 1994\"]}, "
        + "\"publish_places\": [{\"name\": \"New York\"}]}}";
  }
}
