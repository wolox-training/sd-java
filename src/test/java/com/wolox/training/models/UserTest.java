package com.wolox.training.models;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.wolox.training.repositories.UserRepository;
import com.wolox.training.support.factories.UserFactory;
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

public class UserTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User        user;
  private UserFactory userFactory = new UserFactory();

  @Before
  public void init() {
    user = userFactory.createUserWithoutBooks();
    entityManager.persist(user);
    entityManager.flush();
  }

  @Test
  public void whenCreateUser_thenUserIsPersisted() {
    User persistedUser = userRepository.findById(user.getId()).orElse(null);

    assertThat(persistedUser.getUserName(), equalTo(user.getUserName()));
    assertThat(persistedUser.getName(), equalTo(user.getName()));
    assertThat(persistedUser.getBirthDate(), equalTo(user.getBirthDate()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void whenCreateUserWithoutUserName_thenThrowException() {
    user.setUserName(null);
    entityManager.persist(user);
    entityManager.flush();
  }

  @Test(expected = IllegalArgumentException.class)
  public void whenCreateUserWithoutName_thenThrowException() {
    user.setName(null);
    entityManager.persist(user);
    entityManager.flush();
  }

  @Test(expected = IllegalArgumentException.class)
  public void whenCreateBookWithoutBirthday_thenThrowException() {
    user.setBirthDate(null);
    entityManager.persist(user);
    entityManager.flush();
  }

  @Test
  public void whenFindFirstByUserName_thenReturnUser() {
    User userByUserName = userRepository.findFirstByUserName(user.getUserName()).orElse(null);
    assertThat(userByUserName.getUserName(), equalTo(user.getUserName()));
  }

  @Test
  public void whenFindFirstByUserNameWithUnknownUserName_thenReturnNull() {
    User userByUserName = userRepository.findFirstByUserName("n0t 4 n4m3").orElse(null);
    assertThat(userByUserName == null, is(true));
  }
}
