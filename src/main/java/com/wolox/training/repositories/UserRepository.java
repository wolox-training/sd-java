package com.wolox.training.repositories;


import com.wolox.training.models.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findFirstByUsername(String userName);

  List<User> findByBirthDateBetweenAndNameIgnoreCaseContaining(LocalDate startDate,
      LocalDate endDate, CharSequence substring);
}
