package com.figure.figure.repository;

import com.figure.figure.model.Notification;
import com.figure.figure.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    boolean existsByMember_IdAndNews_IdAndTypeAndRevision(Long memberId, Long newsId, NotificationType type, int revision);
    @EntityGraph(attributePaths = {"news", "news.figure"})
    Page<Notification> findByMember_Id(Long memberId, Pageable pageable);
    @EntityGraph(attributePaths = {"news", "news.figure"})
    Optional<Notification> findByIdAndMember_Id(Long id, Long memberId);
    @EntityGraph(attributePaths = "member")
    List<Notification> findByNews_IdAndType(Long newsId, NotificationType type);
}
