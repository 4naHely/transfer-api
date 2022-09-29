package com.ana.transferapi.controller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ana.transferapi.exception.NotFoundException;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.service.ClientAccountService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest({ClientAccountController.class})
class ClientAccountControllerGetClientByAccountNumberTest {

  private static final String URL = "/clientAccount/accounts/1";

  @Autowired protected MockMvc mockMvc;
  @MockBean private ClientAccountService clientAccountService;

  @Test
  void testGetClientByAccountNumberWhenRequestIsValidThenReturnSuccessResponse() throws Exception {
    // Arrange
    doReturn(ClientAccountEntity.builder().id(UUID.randomUUID().toString()).build())
        .when(clientAccountService)
        .getClientByAccountNumber(any());

    // Act
    MockHttpServletRequestBuilder builder =
        get(URL).servletPath(URL).contentType(MediaType.APPLICATION_JSON);

    // Assert
    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.path", is(URL)))
        .andExpect(jsonPath("$.body", notNullValue()))
        .andExpect(jsonPath("$.body.id", notNullValue()));

    verify(clientAccountService).getClientByAccountNumber(any());
  }

  @Test
  void testGetClientByAccountNumberWhenAccountIsNotFoundThenReturnNotFoundExceptionResponse()
      throws Exception {
    // arrange
    doThrow(
            new NotFoundException(
                NotFoundException.NotFoundErrorCodes.ACCOUNT_NOT_FOUND, "Not found account"))
        .when(clientAccountService)
        .getClientByAccountNumber(any());

    // Act
    MockHttpServletRequestBuilder builder =
        get(URL).servletPath(URL).contentType(MediaType.APPLICATION_JSON);

    // assert
    mockMvc.perform(builder).andExpect(status().isNotFound());
    verify(clientAccountService).getClientByAccountNumber(any());
  }
}
