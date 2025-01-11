package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAll(Specification<Location> specification, Pageable pageable);

    List<Location> findAllByCreatedBy(User user);

    @Query(value = "SELECT l.location_id FROM location l WHERE l.name = ?1 AND l.coordinatex = ?2 ORDER BY l.location_id DESC LIMIT 1", nativeQuery = true)
    Optional<Long> findLast(String name, Long x);
}
