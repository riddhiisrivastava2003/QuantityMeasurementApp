package com.riddhi.quantity_service.repository;





import com.riddhi.quantity_service.entity.QuantityOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuantityRepository extends JpaRepository<QuantityOperation, Long> {

}