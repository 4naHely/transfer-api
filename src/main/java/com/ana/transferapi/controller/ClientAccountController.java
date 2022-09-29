/*
 * Controller class for client account´s endpoints.
 */
package com.ana.transferapi.controller;

import com.ana.transferapi.model.RegisterClientRequest;
import com.ana.transferapi.model.Response;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.service.ClientAccountService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RestController
@RequestMapping("/clientAccount")
public class ClientAccountController {

  private final ClientAccountService clientAccountService;

  /**
   * The endpoint is used to register a new client account.
   *
   * @param requestBody Register client endpoint request body parameters.
   * @return JSON response.
   */
  @PostMapping(value = "/register", produces = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Response> registerClient(
      @Valid @RequestBody RegisterClientRequest requestBody, HttpServletRequest request) {
    log.traceEntry("registerClient(requestBody={})", requestBody);

    log.info("Calling client account service to register the new account");

    Response response =
        Response.builder()
            .path(request.getServletPath())
            .statusCode(HttpStatus.CREATED.toString())
            .message("The new account was registered")
            .body(clientAccountService.registerClient(requestBody))
            .build();

    log.info("The new account was registered!");
    return log.traceExit(
        "registerClient(requestBody): {}", new ResponseEntity<>(response, HttpStatus.CREATED));
  }

  /**
   * The endpoint is used to retrieve all client accounts.
   *
   * @param pageable Object containing pagination information.
   * @return JSON response.
   */
  @GetMapping(value = "/accounts", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Page<ClientAccountEntity>> getClients(
      @PageableDefault(page = 0, size = 5, sort = "name") Pageable pageable) {
    log.traceEntry("getClients(pageable={})", pageable);

    log.info("Calling client account service to get all accounts");
    Page<ClientAccountEntity> accounts = clientAccountService.getClients(pageable);

    log.info("Accounts obtained!");
    return log.traceExit(
        "getClients(pageable): {}", ResponseEntity.status(HttpStatus.OK).body(accounts));
  }

  /**
   * The endpoint is used to retrieve a specific client account.
   *
   * @param accountNumber The account number to be search.
   * @return JSON response.
   */
  @GetMapping(value = "/accounts/{accountNumber}", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Response> getClientByAccountNumber(
      @PathVariable(value = "accountNumber") Integer accountNumber, HttpServletRequest request) {
    log.traceEntry("getClientByAccountNumber(accountNumber={})", accountNumber);

    log.info("Calling client account service to get the account");
    Response response =
        Response.builder()
            .path(request.getServletPath())
            .statusCode(HttpStatus.OK.toString())
            .message("Account found!")
            .body(clientAccountService.getClientByAccountNumber(accountNumber))
            .build();

    log.info("Account found!");
    return log.traceExit(
        "getClientByAccountNumber(accountNumber): {}",
        new ResponseEntity<>(response, HttpStatus.OK));
  }
}
