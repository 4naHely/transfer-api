/*
 * Controller class for transfer´s endpoints.
 */
package com.ana.transferapi.controller;

import com.ana.transferapi.model.Response;
import com.ana.transferapi.model.TransferRequest;
import com.ana.transferapi.repository.entity.TransfersEntity;
import com.ana.transferapi.service.TransferService;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController()
@RequestMapping("/transfers")
public class TransferController {

  private final TransferService transferService;

  /**
   * The endpoint is used to handle transfer operation.
   *
   * @param transferRequest Transfer endpoint request body parameters.
   * @return JSON response.
   */
  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Response> transfer(
      @RequestBody TransferRequest transferRequest, HttpServletRequest request) {
    log.traceEntry("transfer(transferRequest={})", transferRequest);

    log.info("Calling transfer service to operate transfer.");
    BigDecimal balanceUpdated = transferService.transfer(transferRequest);

    Response response =
        Response.builder()
            .path(request.getServletPath())
            .statusCode(HttpStatus.OK.toString())
            .message(
                String.format(
                    "Transfer operation completed successfully. actual balance: %f",
                    balanceUpdated))
            .build();

    log.info("Transfer operation completed successfully!");
    return log.traceExit(
        "transfer(transferRequest): {}", new ResponseEntity<>(response, HttpStatus.OK));
  }

  /**
   * The endpoint is used to retrieve the transfer historic related to a specific account.
   *
   * @param accountNumber The account number to be search.
   * @return JSON response.
   */
  @GetMapping("/{accountNumber}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Response> getAccountTransfers(
      @PathVariable(value = "accountNumber") Integer accountNumber, HttpServletRequest request) {
    log.traceEntry("getTransferHistoric(accountNumber={})", accountNumber);

    log.info("Calling transfer service to get all transfers of the account.");
    List<TransfersEntity> listOfTransfers = transferService.getAccountTransfers(accountNumber);

    Response response =
        Response.builder()
            .path(request.getServletPath())
            .statusCode(HttpStatus.OK.toString())
            .body(listOfTransfers)
            .build();

    log.info("Transfers obtained!");
    return log.traceExit(
        "getTransferHistoric(accountNumber): {}", new ResponseEntity<>(response, HttpStatus.OK));
  }
}
