package com.is.lw.core.repository;

import com.is.lw.core.model.Coordinates;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
    List<Coordinates> findAll(Specification<Coordinates> specification, Pageable pageable);
}
