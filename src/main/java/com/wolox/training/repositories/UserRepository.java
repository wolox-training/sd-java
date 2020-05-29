package com.wolox.training.repositories;


import com.wolox.training.models.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findFirstByUsername(String userName);


  @Query("SELECT u FROM User u WHERE "
      + "(:startDate IS NULL OR :endDate IS NULL OR u.birthDate BETWEEN :startDate AND :endDate) AND "
      + "(:name IS NULL OR LOWER(u.name) LIKE  CONCAT('%', LOWER(:name), '%'))")
  List<User> findByBirthDateBetweenAndNameIgnoreCaseContaining(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
      @Param("substring") CharSequence substring
  );
}
