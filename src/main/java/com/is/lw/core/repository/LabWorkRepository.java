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

    @Query("SELECT COUNT(l) FROM LabWork l WHERE l.minimalPoint = :minimalPoint")
    long countByMinimalPoint(@Param("minimalPoint") Integer minimalPoint);

//    CREATE OR REPLACE PROCEDURE delete_laboratory_work_by_minimal_point(target_point NUMERIC, user_id NUMERIC)
//    LANGUAGE plpgsql
//    AS $$
//    BEGIN
//        DELETE FROM lab_work
//        WHERE lab_work_id = (
//                SELECT lab_work_id
//                FROM lab_work
//                WHERE minimal_point = target_point
//                AND created_by = user_id
//                LIMIT 1
//        );
//    END;
//    $$;
    @Modifying
    @Query(value = "CALL delete_laboratory_work_by_minimal_point(?1, ?2)", nativeQuery = true)
    void deleteOneByMinimalPoint(Integer minimalPoint, Long userId);

//    CREATE OR REPLACE PROCEDURE add_lab_work_to_discipline(lw_id NUMERIC, disc_id NUMERIC, user_id NUMERIC)
//    LANGUAGE plpgsql
//    AS $$
//    BEGIN
//    UPDATE lab_work
//    SET discipline_id = disc_id
//    WHERE lab_work_id = lw_id AND created_by = user_id;
//    END;
//    $$;
    @Modifying
    @Query(value = "CALL add_lab_work_to_discipline(?1, ?2, ?3)", nativeQuery = true)
    void addLabWorkToDiscipline(Long labWorkId, Long disciplineId, Long userId);

//    CREATE OR REPLACE PROCEDURE delete_lab_work_to_discipline(lw_id NUMERIC, disc_id NUMERIC, user_id NUMERIC)
//    LANGUAGE plpgsql
//    AS $$
//    BEGIN
//    UPDATE lab_work
//    SET discipline_id = null
//    WHERE lab_work_id = lw_id AND created_by = user_id;
//    END;
//    $$;
    @Modifying
    @Query(value = "CALL delete_lab_work_to_discipline(?1, ?2)", nativeQuery = true)
    void deleteLabWorkToDiscipline(Long labWorkId, Long userId);
}
