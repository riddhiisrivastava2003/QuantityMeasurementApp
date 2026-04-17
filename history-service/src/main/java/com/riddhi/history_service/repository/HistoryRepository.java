package com.riddhi.history_service.repository;

import com.riddhi.history_service.entity.OperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//Spring Data JPA method name convention: findBy[Property]OrderBy[Property][Direction].
// Yeh 'derived query' kehlaata hai — Spring automatically SQL banata hai method name se.
// Manually @Query likhne ki zaroorat nahi.
public interface HistoryRepository extends JpaRepository<OperationHistory, Long> {
    // Generated SQL:
    // SELECT * FROM operation_history
    // WHERE username = ?
    // ORDER BY created_at DESC
    // (newest operations first)

    List<OperationHistory> findByUsernameOrderByCreatedAtDesc(String username);
    void deleteByUsername(String username);
    // Generated SQL:
    // DELETE FROM operation_history WHERE username = ?

}
