package com.ana.transferapi.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ana.transferapi.exception.NotFoundException;
import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientAccountServiceGetClientByAccountNumberTest {

  @Mock private ClientAccountRepository clientAccountRepository;
  private ClientAccountService clientAccountService;

  @BeforeEach
  public void setup() {
    clientAccountService = new ClientAccountService(clientAccountRepository);
  }

  @Test
  void testGetClientsWhenInvokedWithExistingAccountNumberThenReturnTheListOfClientsAccounts() {
    // Arrange
    Integer accountNumber = 2;

    ClientAccountEntity expectedClientAccount =
        ClientAccountEntity.builder()
            .id(UUID.randomUUID().toString())
            .name("Test")
            .accountNumber(1)
            .balance(new BigDecimal("1000"))
            .build();

    doReturn(Optional.of(expectedClientAccount))
        .when(clientAccountRepository)
        .findByAccountNumber(accountNumber);

    // Act
    ClientAccountEntity clientAccount =
        clientAccountService.getClientByAccountNumber(accountNumber);

    // Assert
    Assertions.assertThat(clientAccount).isEqualTo(expectedClientAccount);
    verify(clientAccountRepository).findByAccountNumber(accountNumber);
  }

  @Test
  void testGetClientsWhenAccountNumberDoesNotExistsThenThrowNotFoundException() {
    // Arrange
    Integer accountNumber = 2;
    doReturn(Optional.empty()).when(clientAccountRepository).findByAccountNumber(accountNumber);

    NotFoundException exception =
        new NotFoundException(
            NotFoundException.NotFoundErrorCodes.ACCOUNT_NOT_FOUND,
            String.format("Not found account number: %s", accountNumber));
    // Act
    Throwable result =
        Assertions.catchThrowable(
            () -> clientAccountService.getClientByAccountNumber(accountNumber));

    // Assert
    Assertions.assertThat(result).isEqualTo(exception);
  }
}
