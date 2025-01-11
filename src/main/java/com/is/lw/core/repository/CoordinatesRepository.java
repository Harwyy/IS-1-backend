package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.Coordinates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
    Page<Coordinates> findAll(Pageable pageable);

    List<Coordinates> findAllByCreatedBy(User user);

    @Query(value = "SELECT c.coordinates_id FROM coordinates c WHERE c.x = ?1 AND c.y = ?2 ORDER BY c.coordinates_id DESC LIMIT 1", nativeQuery = true)
    Optional<Long> findLast(float x, Long y);
}
