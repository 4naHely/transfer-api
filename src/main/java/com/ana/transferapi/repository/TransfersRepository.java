/*
 * Repository Database Table client_account
 */
package com.ana.transferapi.repository;

import com.ana.transferapi.repository.entity.TransfersEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfersRepository extends JpaRepository<TransfersEntity, Integer> {
  List<TransfersEntity> findByAccountAccountNumberOrderByDateDesc(Integer accountNumber);
}
