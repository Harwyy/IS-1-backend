package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.Location;
import com.is.lw.core.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying
    @Query("UPDATE Person p SET p.location = null WHERE p.location.id = :locationId")
    void updatePersonsLocationToNull(@Param("locationId") Long locationId);

    List<Person> findAll(Specification<Person> specification, Pageable pageable);

    List<Person> findAllByCreatedBy(User user);

    @Query(value = "SELECT p.person_id FROM person p WHERE p.name = ?1 AND p.hair_color = ?2 ORDER BY p.person_id DESC LIMIT 1", nativeQuery = true)
    Optional<Long> findLast(String name, String hairColor);
}
