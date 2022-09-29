package com.ana.transferapi.repository;

import com.ana.transferapi.AbstractItSupport;
import com.ana.transferapi.enums.TransferStatusEnum;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.repository.entity.TransfersEntity;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TransferHistoricRepositoryTestIT extends AbstractItSupport {

  @Test
  public void testFindByAccountAccountNumberOrderByDateDesc() {
    // Arrange
    ClientAccountEntity originAccount = buildAndSaveClientAccountEntity(c -> c);
    ClientAccountEntity destinationAccount = buildAndSaveClientAccountEntity(c -> c);

    TransfersEntity transferHistoric =
        TransfersEntity.builder()
            .status(TransferStatusEnum.SUCCESS)
            .date(LocalDateTime.now())
            .amount(new BigDecimal("10.00"))
            .destinationAccountNumber(destinationAccount.getAccountNumber())
            .account(originAccount)
            .build();

    TransfersEntity transferHistoric2 =
        TransfersEntity.builder()
            .status(TransferStatusEnum.SUCCESS)
            .date(LocalDateTime.now().plus(Duration.ofDays(1)))
            .amount(new BigDecimal("10.00"))
            .destinationAccountNumber(destinationAccount.getAccountNumber())
            .account(originAccount)
            .build();

    // Act
    transferHistoricRepository.save(transferHistoric);
    transferHistoricRepository.save(transferHistoric2);

    // Assert
    List<TransfersEntity> result =
        transferHistoricRepository.findByAccountAccountNumberOrderByDateDesc(
            originAccount.getAccountNumber());
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result.get(0)).isEqualTo(transferHistoric2);
    Assertions.assertThat(result.get(1)).isEqualTo(transferHistoric);
  }
}
