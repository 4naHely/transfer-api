package com.ana.transferapi.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ana.transferapi.enums.TransferStatusEnum;
import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.TransfersRepository;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.repository.entity.TransfersEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferServiceGetAccountTransfersTest {

  @Mock private TransfersRepository transferRepository;
  @Mock private ClientAccountRepository clientAccountRepository;

  @Test
  void testGetAccountTransfersWhenInvokedThenReturnTheTransfersList() {
    // Arrange
    Integer accountNumber = 2;
    TransferService transferService =
        new TransferService(transferRepository, clientAccountRepository);

    List<TransfersEntity> expectedList =
        List.of(
            TransfersEntity.builder()
                .id(1)
                .status(TransferStatusEnum.SUCCESS)
                .date(LocalDateTime.now())
                .amount(BigDecimal.valueOf(1000))
                .destinationAccountNumber(1)
                .account(ClientAccountEntity.builder().id(UUID.randomUUID().toString()).build())
                .build());

    doReturn(expectedList)
        .when(transferRepository)
        .findByAccountAccountNumberOrderByDateDesc(accountNumber);

    // Act
    List<TransfersEntity> transferList = transferService.getAccountTransfers(accountNumber);

    // Assert
    Assertions.assertThat(transferList).isEqualTo(expectedList);
    verify(transferRepository).findByAccountAccountNumberOrderByDateDesc(accountNumber);
  }
}
