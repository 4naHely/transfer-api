package com.ana.transferapi.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ClientAccountServiceGetClientsTest {

  @Mock private ClientAccountRepository clientAccountRepository;
  @Mock private Pageable pageable;

  @Test
  void testRegisterClientWhenInvokedThenReturnTheNewAccountCreated() {
    // Arrange
    ClientAccountService clientAccountService = new ClientAccountService(clientAccountRepository);
    doReturn(Page.empty()).when(clientAccountRepository).findAll(pageable);

    // Act
    Page<ClientAccountEntity> clientAccount = clientAccountService.getClients(pageable);

    // Assert
    verify(clientAccountRepository).findAll(pageable);
    Assertions.assertThat(clientAccount).isEqualTo(Page.empty());
  }
}
