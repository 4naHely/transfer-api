package com.ana.transferapi.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ana.transferapi.service.ClientAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest({ClientAccountController.class})
class ClientAccountControllerGetClientsTest {

  private static final String URL = "/clientAccount/accounts";

  @Autowired protected MockMvc mockMvc;
  @MockBean private ClientAccountService clientAccountService;

  @Test
  void testGetClientsWhenRequestIsValidThenReturnSuccessResponse() throws Exception {
    // Arrange
    doReturn(Page.empty()).when(clientAccountService).getClients(any());

    // Act
    MockHttpServletRequestBuilder builder =
        get(URL).servletPath(URL).contentType(MediaType.APPLICATION_JSON);

    // Assert
    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", notNullValue()));

    verify(clientAccountService).getClients(any());
  }
}
