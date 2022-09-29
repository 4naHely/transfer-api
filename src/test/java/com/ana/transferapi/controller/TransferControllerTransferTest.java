package com.ana.transferapi.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ana.transferapi.model.TransferRequest;
import com.ana.transferapi.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest({TransferController.class})
class TransferControllerTransferTest {

  private static final String URL = "/transfers";

  @Autowired protected MockMvc mockMvc;
  @MockBean private TransferService transferService;

  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void setup() {}

  @Test
  void testTransferWhenRequestIsValidThenReturnSuccessResponse() throws Exception {
    // Arrange
    TransferRequest transferRequest =
        TransferRequest.builder()
            .originAccount(2)
            .destinationAccount(1)
            .amount(BigDecimal.valueOf(100))
            .build();

    doReturn(new BigDecimal("20")).when(transferService).transfer(transferRequest);

    // Act
    MockHttpServletRequestBuilder builder =
        post(URL)
            .servletPath(URL)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(transferRequest));

    // Assert
    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.path", is(URL)))
        .andExpect(jsonPath("$.message", notNullValue()));

    verify(transferService).transfer(any());
  }
}
