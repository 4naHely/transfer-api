package com.ana.transferapi.service;

import com.ana.transferapi.AbstractItSupport;
import com.ana.transferapi.exception.NotFoundException;
import com.ana.transferapi.model.RegisterClientRequest;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientAccountServiceTestIT extends AbstractItSupport {

  @Autowired ClientAccountService subject;

  @Test
  public void testRegisterClientWhenGivenAValidRequestThenSaveTheNewAccount() {
    // Arrange
    String accountName = "test";
    BigDecimal balanceAccount = new BigDecimal("0.00");

    RegisterClientRequest clientAccount =
        RegisterClientRequest.builder().name(accountName).balance(balanceAccount).build();

    ClientAccountEntity resultFromFunction = subject.registerClient(clientAccount);

    // Act
    List<ClientAccountEntity> result = clientAccountRepository.findAll();

    // Assert
    Assertions.assertThat(result).hasSize(1);
    Assertions.assertThat(result).contains(resultFromFunction);
    Assertions.assertThat(result.get(0).getName()).isEqualTo(accountName);
    Assertions.assertThat(result.get(0).getBalance()).isEqualTo(balanceAccount);
  }

  @Test
  public void testGetClientByAccountNumberWhenAccountExistsThenReturnTheFoundedAccount() {
    // Arrange
    ClientAccountEntity clientAccount = buildAndSaveClientAccountEntity(c -> c);

    // Act
    ClientAccountEntity result = subject.getClientByAccountNumber(clientAccount.getAccountNumber());

    // Assert
    Assertions.assertThat(result).isEqualTo(clientAccount);
  }

  @Test
  public void testGetClientByAccountNumberWhenAccountNotExistsThenThrowError() {
    // Act
    Throwable result = Assertions.catchThrowable(() -> subject.getClientByAccountNumber(1));

    // Assert
    Assertions.assertThat(result).isInstanceOf(NotFoundException.class);
    Assertions.assertThat(result).hasMessageContaining("Not found account number: 1");
  }
}
