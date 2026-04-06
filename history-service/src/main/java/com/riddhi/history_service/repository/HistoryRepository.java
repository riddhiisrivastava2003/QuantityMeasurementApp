package com.riddhi.history_service.repository;

import com.riddhi.history_service.entity.OperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<OperationHistory, Long> {
    List<OperationHistory> findByUsernameOrderByCreatedAtDesc(String username);
    void deleteByUsername(String username);
}
