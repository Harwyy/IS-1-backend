package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.Discipline;
import com.is.lw.core.model.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    List<Discipline> findAll(Specification<Discipline> specification, Pageable pageable);

    List<Discipline> findAllByCreatedBy(User user);
}
