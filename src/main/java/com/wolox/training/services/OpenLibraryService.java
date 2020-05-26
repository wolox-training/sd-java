package com.wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.training.DAO.ExternalBookInfoDTO;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenLibraryService {

  @Autowired
  BookRepository bookRepository;

  private RestTemplate restTemplate = new RestTemplate();
  private ObjectMapper mapper = new ObjectMapper();

  public Book bookInfo(String ISBN) throws JsonProcessingException {
    String response = getBookFromExternalService(ISBN);
    if (getBookFromExternalService(ISBN).equals("{}")) {
      throw new NotFoundException("Book with ISBN: " + ISBN + "not found");
    } else {
      return processBook(mapResponseToObject(ISBN, response));
    }
  }

  private String getBookFromExternalService(String ISBN) {
    final String url =
        "https://openlibrary.org/api/books?bibkeys=ISBN:" + ISBN + "&format=json&jscmd=data";
    return restTemplate.getForObject(url, String.class);
  }

  private ExternalBookInfoDTO mapResponseToObject(String ISBN, String response)
      throws JsonProcessingException {
    final String mainKey = "ISBN:" + ISBN;

    String jsonString = mapper
        .writeValueAsString(mapper.readValue(response, HashMap.class).get(mainKey));
    HashMap bookHash = mapper.readValue(jsonString, HashMap.class);

    ExternalBookInfoDTO book = mapper.convertValue(bookHash, ExternalBookInfoDTO.class);
    book.setISBN(ISBN);
    return book;
  }

  private Book processBook(ExternalBookInfoDTO book) {
    if (bookRepository.findFirstByIsbn(book.getISBN()) == null) {
      return bookRepository.save(externalBookToLocalBook(book));
    } else {
      return externalBookToLocalBook(book);
    }
  }

  private Book externalBookToLocalBook(ExternalBookInfoDTO externalBook) {
    Book book = new Book();
    book.setAuthor(externalBook.getAuthors()[0].getName());
    book.setIsbn(externalBook.getISBN());
    book.setTitle(externalBook.getTitle());
    book.setSubtitle(externalBook.getSubtitle());
    book.setPages(externalBook.getNumberOfPages());
    book.setPublisher(externalBook.getPublishers()[0].getName());
    book.setYear(externalBook.getPublishDate());
    book.setImage(externalBook.getAuthors()[0].getUrl());
    return book;
  }
}
