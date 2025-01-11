package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.Discipline;
import com.is.lw.core.model.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    List<Discipline> findAll(Specification<Discipline> specification, Pageable pageable);

    List<Discipline> findAllByCreatedBy(User user);

    @Query(value = "SELECT d.discipline_id FROM discipline d WHERE d.name = ?1 AND d.lecture_hours = ?2 AND d.self_study_hours = ?3 ORDER BY d.discipline_id DESC LIMIT 1", nativeQuery = true)
    Optional<Long> findLast(String name, Long lh, Integer ssh);
}
