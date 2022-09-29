package com.ana.transferapi.controller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ana.transferapi.model.RegisterClientRequest;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.service.ClientAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest({ClientAccountController.class})
class ClientAccountControllerRegisterClientTest {

  private static final String URL = "/clientAccount/register";

  @Autowired protected MockMvc mockMvc;
  @MockBean private ClientAccountService clientAccountService;

  private static RegisterClientRequest registerClientRequest;
  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void setup() {
    registerClientRequest =
        RegisterClientRequest.builder().name("Ana").balance(BigDecimal.valueOf(100)).build();

    ClientAccountEntity createdAccount =
        ClientAccountEntity.builder()
            .id(UUID.randomUUID().toString())
            .name(registerClientRequest.getName())
            .accountNumber(1)
            .balance(registerClientRequest.getBalance())
            .build();

    doReturn(createdAccount).when(clientAccountService).registerClient(registerClientRequest);
  }

  @Test
  void testRegisterClientWhenRequestIsValidThenReturnSuccessResponse() throws Exception {
    // Act
    MockHttpServletRequestBuilder builder =
        post(URL)
            .servletPath(URL)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(registerClientRequest));

    // Assert
    mockMvc
        .perform(builder)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.path", is(URL)))
        .andExpect(jsonPath("$.body", notNullValue()))
        .andExpect(jsonPath("$.body.id", notNullValue()));

    verify(clientAccountService).registerClient(registerClientRequest);
  }

  @Test
  void testRegisterClientWhenRequestNameIsBlankThenReturnBadRequestResponse() throws Exception {
    // Arrange
    registerClientRequest.setName(null);

    // Act
    MockHttpServletRequestBuilder builder =
        post(URL)
            .servletPath(URL)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(registerClientRequest));

    // Assert
    mockMvc.perform(builder).andExpect(status().isBadRequest());

    verify(clientAccountService, never()).registerClient(any());
  }
}
