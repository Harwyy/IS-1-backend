package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAll(Specification<Location> specification, Pageable pageable);

    List<Location> findAllByCreatedBy(User user);
}
