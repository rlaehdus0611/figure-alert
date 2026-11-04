package com.figure.figure.repository;

import com.figure.figure.model.OfficialNews;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface OfficialNewsRepository extends JpaRepository<OfficialNews, Long> {
    Optional<OfficialNews> findByEventKey(String eventKey);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select n from OfficialNews n where n.id = :id")
    Optional<OfficialNews> findLocked(@Param("id") Long id);
}
