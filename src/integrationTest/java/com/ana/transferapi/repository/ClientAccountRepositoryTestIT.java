package com.ana.transferapi.repository;

import com.ana.transferapi.AbstractItSupport;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ClientAccountRepositoryTestIT extends AbstractItSupport {

  @Test
  public void testFindByAccountNumber() {
    // Arrange
    ClientAccountEntity clientAccount = buildAndSaveClientAccountEntity(c -> c);

    // Act
    Optional<ClientAccountEntity> result =
        clientAccountRepository.findByAccountNumber(clientAccount.getAccountNumber());

    // Assert
    Assertions.assertThat(result).contains(clientAccount);
  }

  @Test
  public void testDoNotFindByAccountNumberWhenThereIsNoAccount() {
    // Act
    Optional<ClientAccountEntity> result = clientAccountRepository.findByAccountNumber(1);

    // Assert
    Assertions.assertThat(result).isEmpty();
  }
}
