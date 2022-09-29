/*
 * Holds the operations related to client accounts.
 */
package com.ana.transferapi.service;

import com.ana.transferapi.exception.NotFoundException;
import com.ana.transferapi.exception.NotFoundException.NotFoundErrorCodes;
import com.ana.transferapi.model.RegisterClientRequest;
import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ClientAccountService {

  private final ClientAccountRepository clientAccountRepository;

  /**
   * Method responsible to register new accounts.
   *
   * @param registerClientRequest The request object containing the account info.
   * @return The created account.
   */
  @Transactional
  public ClientAccountEntity registerClient(RegisterClientRequest registerClientRequest) {
    log.traceEntry("registerClient(registerClientRequest={})", registerClientRequest);

    return log.traceExit(
        "registerClient(registerClientRequest): {}",
        clientAccountRepository.save(
            ClientAccountEntity.builder()
                .name(registerClientRequest.getName())
                .balance(registerClientRequest.getBalance())
                .build()));
  }

  /**
   * Method responsible to retrieve all accounts registered.
   *
   * @param pageable Object containing pagination information.
   * @return A {@link Page} sublist of a list of the accounts found.
   */
  public Page<ClientAccountEntity> getClients(Pageable pageable) {
    log.traceEntry("getClients(pageable={})", pageable);

    return log.traceExit("getClients(pageable): {}", clientAccountRepository.findAll(pageable));
  }

  /**
   * Method responsible to find the client account by account number.
   *
   * @param accountNumber The account number to be search.
   * @return The account found.
   */
  public ClientAccountEntity getClientByAccountNumber(Integer accountNumber) {
    log.traceEntry("getClientByAccountNumber(accountNumber={})", accountNumber);

    return log.traceExit(
        "getClientByAccountNumber(accountNumber): {}",
        clientAccountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(
                () -> {
                  log.error("Could not find the account: {}", accountNumber);
                  return new NotFoundException(
                      NotFoundErrorCodes.ACCOUNT_NOT_FOUND,
                      String.format("Not found account number: %s", accountNumber));
                }));
  }
}
