package com.ana.transferapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ana.transferapi.model.RegisterClientRequest;
import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import java.math.BigDecimal;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientAccountServiceRegisterClientTest {

  @Mock private ClientAccountRepository clientAccountRepository;


  @Test
  void testGetClientsWhenInvokedThenReturnTheListOfClientsAccounts() {
    // Arrange
    ClientAccountService clientAccountService = new ClientAccountService(clientAccountRepository);

    RegisterClientRequest registerClientRequest =
            RegisterClientRequest.builder().name("Test").balance(new BigDecimal("100")).build();

    ClientAccountEntity expectedClientAccount =
            ClientAccountEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .name(registerClientRequest.getName())
                    .accountNumber(1)
                    .balance(registerClientRequest.getBalance())
                    .build();

    doReturn(expectedClientAccount).when(clientAccountRepository).save(any());

    // Act
    ClientAccountEntity clientAccount = clientAccountService.registerClient(registerClientRequest);

    // Assert
    Assertions.assertThat(clientAccount).isEqualTo(expectedClientAccount);
    verify(clientAccountRepository).save(any());
  }
}
