package com.ana.transferapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ana.transferapi.exception.BadRequestException;
import com.ana.transferapi.exception.ConflictException;
import com.ana.transferapi.exception.NotFoundException;
import com.ana.transferapi.model.TransferRequest;
import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.TransfersRepository;
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
class TransferServiceTransferTest {

  @Mock private TransfersRepository transferRepository;
  @Mock private ClientAccountRepository clientAccountRepository;

  private TransferService transferService;
  private TransferRequest transferRequest;
  private static final ClientAccountEntity destinationAccount =
      ClientAccountEntity.builder()
          .id(UUID.randomUUID().toString())
          .accountNumber(1)
          .name("Test2")
          .balance(new BigDecimal("100"))
          .build();

  @BeforeEach
  public void setup() {
    transferService = new TransferService(transferRepository, clientAccountRepository);

    transferRequest =
        TransferRequest.builder()
            .destinationAccount(1)
            .originAccount(2)
            .amount(new BigDecimal("100"))
            .build();
  }

  @Test
  void testTransferWhenTransferRequestIsValidAndOriginHasEnoughBalanceThenUpdateBalances() {
    // Arrange
    BigDecimal expectedValue = new BigDecimal("100");

    ClientAccountEntity originAccount =
        ClientAccountEntity.builder()
            .id(UUID.randomUUID().toString())
            .accountNumber(2)
            .name("Test1")
            .balance(new BigDecimal("200"))
            .build();

    doAnswer(invocation -> Optional.of(originAccount))
        .when(clientAccountRepository)
        .findByAccountNumber(transferRequest.getOriginAccount());

    doAnswer(invocation -> Optional.of(destinationAccount))
        .when(clientAccountRepository)
        .findByAccountNumber(transferRequest.getDestinationAccount());

    // Act
    BigDecimal actualBalance = transferService.transfer(transferRequest);

    // Assert
    Assertions.assertThat(actualBalance).isEqualTo(expectedValue);
    verify(clientAccountRepository, times(2)).save(any());
    verify(transferRepository).save(any());
  }

  @Test
  void testTransferWhenOriginAccountIsNotFoundThenThrowBadRequestException() {
    // Arrange
    doAnswer(invocation -> Optional.empty())
        .when(clientAccountRepository)
        .findByAccountNumber(transferRequest.getOriginAccount());

    NotFoundException exception =
        new NotFoundException(
            NotFoundException.NotFoundErrorCodes.ORIGIN_ACCOUNT_NOT_FOUND,
            String.format("Not found account number: %s", transferRequest.getOriginAccount()));

    // Act
    Throwable result = Assertions.catchThrowable(() -> transferService.transfer(transferRequest));

    // Assert
    Assertions.assertThat(result).isEqualTo(exception);
  }

  @Test
  void testTransferWhenTransferRequestHasAmountGreaterThan1000ThenThrowBadRequestException() {
    // Arrange
    transferRequest.setAmount(new BigDecimal("1001"));

    ClientAccountEntity originAccount =
        ClientAccountEntity.builder()
            .id(UUID.randomUUID().toString())
            .accountNumber(2)
            .name("Test1")
            .balance(new BigDecimal("200"))
            .build();

    doAnswer(invocation -> Optional.of(originAccount))
        .when(clientAccountRepository)
        .findByAccountNumber(transferRequest.getOriginAccount());

    BadRequestException exception =
        new BadRequestException(
            BadRequestException.BadRequestErrorsTypes.TRANSFER_AMOUNT_LIMIT_ACHIEVED,
            "the transfer amount must be a maximum of R$ 1000.00");

    // Act
    Throwable result = Assertions.catchThrowable(() -> transferService.transfer(transferRequest));

    // Assert
    Assertions.assertThat(result).isEqualTo(exception);
  }

  @Test
  void testTransferWhenOriginHasNotEnoughBalanceThenThrowConflictException() {
    // Arrange
    ClientAccountEntity originAccount =
        ClientAccountEntity.builder()
            .id(UUID.randomUUID().toString())
            .accountNumber(2)
            .name("Test1")
            .balance(new BigDecimal("0"))
            .build();

    doAnswer(invocation -> Optional.of(originAccount))
        .when(clientAccountRepository)
        .findByAccountNumber(transferRequest.getOriginAccount());

    ConflictException exception =
        new ConflictException(
            ConflictException.ConflictErrorsTypes.INSUFFICIENT_ACCOUNT_BALANCE,
            String.format(
                "the account %s has not enough balance for the operation",
                transferRequest.getOriginAccount()));

    // Act
    Throwable result = Assertions.catchThrowable(() -> transferService.transfer(transferRequest));

    // Assert
    Assertions.assertThat(result).isEqualTo(exception);
  }

  @Test
  void testTransferWhenDestinationAccountIsNotFoundThenThrowNotFoundException() {
    // Arrange
    ClientAccountEntity originAccount =
        ClientAccountEntity.builder()
            .id(UUID.randomUUID().toString())
            .accountNumber(2)
            .name("Test1")
            .balance(new BigDecimal("200"))
            .build();

    doAnswer(invocation -> Optional.of(originAccount))
        .when(clientAccountRepository)
        .findByAccountNumber(transferRequest.getOriginAccount());

    doAnswer(invocation -> Optional.empty())
        .when(clientAccountRepository)
        .findByAccountNumber(transferRequest.getDestinationAccount());

    NotFoundException exception =
        new NotFoundException(
            NotFoundException.NotFoundErrorCodes.DESTINATION_ACCOUNT_NOT_FOUND,
            String.format(
                "Not found destination account number: %s",
                transferRequest.getDestinationAccount()));

    // Act
    Throwable result = Assertions.catchThrowable(() -> transferService.transfer(transferRequest));

    // Assert
    Assertions.assertThat(result).isEqualTo(exception);
  }
}
