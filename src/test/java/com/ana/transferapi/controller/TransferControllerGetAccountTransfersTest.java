package com.ana.transferapi.controller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ana.transferapi.service.TransferService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest({TransferController.class})
class TransferControllerGetAccountTransfersTest {

  private static final String URL = "/transfers/1";

  @Autowired protected MockMvc mockMvc;
  @MockBean private TransferService transferService;

  @Test
  void testGetAccountTransfersWhenRequestIsValidThenReturnSuccessResponse() throws Exception {
    /// Arrange
    doReturn(Collections.emptyList()).when(transferService).getAccountTransfers(any());

    // Act
    MockHttpServletRequestBuilder builder =
        get(URL).servletPath(URL).contentType(MediaType.APPLICATION_JSON);

    // Assert
    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.path", is(URL)))
        .andExpect(jsonPath("$.body", notNullValue()));

    verify(transferService).getAccountTransfers(any());
  }
}
