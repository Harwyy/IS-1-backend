package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.Coordinates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
    Page<Coordinates> findAll(Pageable pageable);

    List<Coordinates> findAllByCreatedBy(User user);
}
