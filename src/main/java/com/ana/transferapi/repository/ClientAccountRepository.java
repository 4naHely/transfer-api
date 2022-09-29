/*
 * Repository Database Table transfer_historic
 */
package com.ana.transferapi.repository;

import com.ana.transferapi.repository.entity.ClientAccountEntity;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccountEntity, String> {

  Optional<ClientAccountEntity> findByAccountNumber(Integer accountNumber);

//  @Lock(LockModeType.PESSIMISTIC_WRITE)
//  @Query("SELECT a FROM ClientAccountEntity a WHERE a.accountNumber = :accountNumber")
//  @Transactional
  //Optional<ClientAccountEntity> findByAccountNumberWithPessimisticLock(Integer accountNumber);
}
