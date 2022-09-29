package com.ana.transferapi.service;

import com.ana.transferapi.AbstractItSupport;
import com.ana.transferapi.enums.TransferStatusEnum;
import com.ana.transferapi.exception.BadRequestException;
import com.ana.transferapi.exception.ConflictException;
import com.ana.transferapi.exception.NotFoundException;
import com.ana.transferapi.model.TransferRequest;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.repository.entity.TransfersEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransferServiceTestIT extends AbstractItSupport {

  @Autowired TransferService subject;

  @Test
  public void testTransferWhenOriginAccountNotFoundedThenThrowNotFoundException() {
    // Arrange
    TransferRequest transferRequest =
        TransferRequest.builder()
            .originAccount(1)
            .destinationAccount(2)
            .amount(new BigDecimal("10.00"))
            .build();

    // Act
    Throwable result = Assertions.catchThrowable(() -> subject.transfer(transferRequest));

    // Assert
    Assertions.assertThat(result).isInstanceOf(NotFoundException.class);
    Assertions.assertThat(result).hasMessageContaining("Not found account number: 1");
  }

  @Test
  public void testTransferWhenAmountIsGreaterThan1000ThenThrowBadRequestException() {
    // Arrange
    ClientAccountEntity originAccount =
        buildAndSaveClientAccountEntity(c -> c.balance(new BigDecimal("5000.00")));
    ClientAccountEntity destinationAccount = buildAndSaveClientAccountEntity(c -> c);
    TransferRequest transferRequest =
        TransferRequest.builder()
            .originAccount(originAccount.getAccountNumber())
            .destinationAccount(destinationAccount.getAccountNumber())
            .amount(new BigDecimal("1001.00"))
            .build();

    // Act
    Throwable throwResult = Assertions.catchThrowable(() -> subject.transfer(transferRequest));

    // Assert
    Assertions.assertThat(throwResult).isInstanceOf(BadRequestException.class);
    Assertions.assertThat(throwResult)
        .hasMessageContaining("the transfer amount must be a maximum of R$ 1000.00");
    List<TransfersEntity> result = transferHistoricRepository.findAll();
    Assertions.assertThat(result).hasSize(1);
    Assertions.assertThat(result.get(0).getAccount()).isEqualTo(originAccount);
    Assertions.assertThat(result.get(0).getDestinationAccountNumber())
        .isEqualTo(destinationAccount.getAccountNumber());
    Assertions.assertThat(result.get(0).getAmount()).isEqualTo(transferRequest.getAmount());
    Assertions.assertThat(result.get(0).getStatus()).isEqualTo(TransferStatusEnum.FAILED);
  }

  @Test
  public void testTransferWhenAccountHasNotEnoughBalanceThenThrowBadRequestException() {
    // Arrange
    ClientAccountEntity originAccount =
        buildAndSaveClientAccountEntity(c -> c.balance(new BigDecimal("10.00")));
    ClientAccountEntity destinationAccount = buildAndSaveClientAccountEntity(c -> c);
    TransferRequest transferRequest =
        TransferRequest.builder()
            .originAccount(originAccount.getAccountNumber())
            .destinationAccount(destinationAccount.getAccountNumber())
            .amount(new BigDecimal("1000.00"))
            .build();

    Throwable throwResult = Assertions.catchThrowable(() -> subject.transfer(transferRequest));
    Assertions.assertThat(throwResult).isInstanceOf(ConflictException.class);
    Assertions.assertThat(throwResult)
        .hasMessageContaining(
            String.format(
                "the account %s has not enough balance for the operation",
                transferRequest.getOriginAccount()));

    // Act
    List<TransfersEntity> result = transferHistoricRepository.findAll();

    // Assert
    Assertions.assertThat(result).hasSize(1);
    Assertions.assertThat(result.get(0).getAccount()).isEqualTo(originAccount);
    Assertions.assertThat(result.get(0).getDestinationAccountNumber())
        .isEqualTo(destinationAccount.getAccountNumber());
    Assertions.assertThat(result.get(0).getAmount()).isEqualTo(transferRequest.getAmount());
    Assertions.assertThat(result.get(0).getStatus()).isEqualTo(TransferStatusEnum.FAILED);
  }

  @Test
  public void testTransferWhenDestinationAccountNotFoundedThenThrowNotFoundException() {
    // Arrange
    ClientAccountEntity originAccount =
        buildAndSaveClientAccountEntity(c -> c.balance(new BigDecimal("10.00")));
    TransferRequest transferRequest =
        TransferRequest.builder()
            .originAccount(originAccount.getAccountNumber())
            .destinationAccount(2)
            .amount(new BigDecimal("1.00"))
            .build();

    // Act
    Throwable throwResult = Assertions.catchThrowable(() -> subject.transfer(transferRequest));

    // Assert
    Assertions.assertThat(throwResult).isInstanceOf(NotFoundException.class);
    Assertions.assertThat(throwResult)
        .hasMessageContaining(
            String.format(
                "Not found destination account number: %s",
                transferRequest.getDestinationAccount()));
    List<TransfersEntity> result = transferHistoricRepository.findAll();
    Assertions.assertThat(result).hasSize(1);
    Assertions.assertThat(result.get(0).getAccount()).isEqualTo(originAccount);
    Assertions.assertThat(result.get(0).getDestinationAccountNumber())
        .isEqualTo(transferRequest.getDestinationAccount());
    Assertions.assertThat(result.get(0).getAmount()).isEqualTo(transferRequest.getAmount());
    Assertions.assertThat(result.get(0).getStatus()).isEqualTo(TransferStatusEnum.FAILED);
  }

  @Test
  public void testTransferWhenIsValidTransferThenBalanceAreUpdated() {
    // Arrange
    ClientAccountEntity originAccount =
        buildAndSaveClientAccountEntity(c -> c.balance(new BigDecimal("10.00")));
    ClientAccountEntity destinationAccount = buildAndSaveClientAccountEntity(c -> c);
    BigDecimal transferAmount = new BigDecimal("1.00");

    TransferRequest transferRequest =
        TransferRequest.builder()
            .originAccount(originAccount.getAccountNumber())
            .destinationAccount(destinationAccount.getAccountNumber())
            .amount(transferAmount)
            .build();

    // Act
    subject.transfer(transferRequest);


    // Assert
    BigDecimal expectedOriginAccountBalance = originAccount.getBalance().subtract(transferAmount);
    BigDecimal expectedDestinationAccountBalance =
        destinationAccount.getBalance().add(transferAmount);
    originAccount.setBalance(expectedOriginAccountBalance);

    List<TransfersEntity> result = transferHistoricRepository.findAll();
    Assertions.assertThat(result).hasSize(1);
    Assertions.assertThat(result.get(0).getAccount()).isEqualTo(originAccount);
    Assertions.assertThat(result.get(0).getDestinationAccountNumber())
        .isEqualTo(transferRequest.getDestinationAccount());
    Assertions.assertThat(result.get(0).getAmount()).isEqualTo(transferRequest.getAmount());
    Assertions.assertThat(result.get(0).getStatus()).isEqualTo(TransferStatusEnum.SUCCESS);

    ClientAccountEntity originAccountResult =
        clientAccountRepository.findByAccountNumber(originAccount.getAccountNumber()).get();
    ClientAccountEntity destinationAccountResult =
        clientAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber()).get();

    Assertions.assertThat(originAccountResult.getBalance()).isEqualTo(expectedOriginAccountBalance);
    Assertions.assertThat(destinationAccountResult.getBalance())
        .isEqualTo(expectedDestinationAccountBalance);
  }

  @Test
  public void testTransferWhenReceivingRequestsAtTheSameTimeThenShouldKeepTheDataConsistent() {
    // Arrange
    ClientAccountEntity originAccount =
        buildAndSaveClientAccountEntity(c -> c.balance(new BigDecimal("10.00")));
    ClientAccountEntity destinationAccount = buildAndSaveClientAccountEntity(c -> c);
    BigDecimal transferAmount = new BigDecimal("1.00");

    TransferRequest transferRequest =
        TransferRequest.builder()
            .originAccount(originAccount.getAccountNumber())
            .destinationAccount(destinationAccount.getAccountNumber())
            .amount(transferAmount)
            .build();

    // Act
    List<BigDecimal> returnedResult =
        Stream.<Supplier<BigDecimal>>of(
                () -> subject.transfer(transferRequest), () -> subject.transfer(transferRequest))
            .parallel()
            .map(Supplier::get)
            .collect(Collectors.toList());

    // Assert
    ClientAccountEntity result =
        clientAccountRepository.findByAccountNumber(originAccount.getAccountNumber()).get();
    Assertions.assertThat(returnedResult).contains(new BigDecimal("8.00"));
    Assertions.assertThat(result.getBalance()).isEqualTo(new BigDecimal("8.00"));
  }
}
