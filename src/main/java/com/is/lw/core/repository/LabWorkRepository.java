package com.is.lw.core.repository;

import com.is.lw.auth.model.User;
import com.is.lw.core.model.LabWork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {

    @Modifying
    @Query("UPDATE LabWork l SET l.author = null WHERE l.author.id = :personId")
    void updateLabWorksPersonToNull(@Param("personId") Long personId);

    @Modifying
    @Query("UPDATE LabWork l SET l.coordinates = null WHERE l.coordinates.id = :coordinatesId")
    void updateLabWorksCoordinatesToNull(@Param("coordinatesId") Long coordinatesId);

    @Modifying
    @Query("UPDATE LabWork l SET l.discipline = null WHERE l.discipline.id = :disciplineId")
    void updateLabWorksDisciplineToNull(@Param("disciplineId") Long disciplineId);

    List<LabWork> findAll(Specification<LabWork> specification, Pageable pageable);

    List<LabWork> findAllByCreatedBy(User user);
}
