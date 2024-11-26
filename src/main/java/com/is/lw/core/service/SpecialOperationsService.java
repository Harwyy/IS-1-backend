package com.is.lw.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class SpecialOperationsService {

//    private final EntityManager em;
//
//    @Transactional
//    public void deleteOneByMinimalPoint(Double minimalPoint) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        em.createNativeQuery("SELECT delete_object_by_minimal_point(:minimalPoint, :owner)")
//                .setParameter("minimalPoint", minimalPoint)
//                .setParameter("owner", user.getId())
//                .executeUpdate();
//
//    }
//
//    public Long countByMinimalPointGreaterThan(Double minimalPoint) {
//        return 1L;
//    }
//
//    public List<Object> findByDescriptionContains(String substring) {
//        return null;
//    }
}
