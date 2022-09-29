/*
 * Holds the operations related to transfer between accounts.
 */
package com.ana.transferapi.service;

import com.ana.transferapi.enums.TransferStatusEnum;
import com.ana.transferapi.exception.BadRequestException;
import com.ana.transferapi.exception.BadRequestException.BadRequestErrorsTypes;
import com.ana.transferapi.exception.ConflictException;
import com.ana.transferapi.exception.ConflictException.ConflictErrorsTypes;
import com.ana.transferapi.exception.NotFoundException;
import com.ana.transferapi.exception.NotFoundException.NotFoundErrorCodes;
import com.ana.transferapi.model.TransferRequest;
import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.TransfersRepository;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.repository.entity.TransfersEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class TransferService {

  private final TransfersRepository transferRepository;
  private final ClientAccountRepository clientAccountRepository;

  /**
   * Method responsible to get the transfers related to an account.
   *
   * @param accountNumber The account number to be search.
   * @return List of transfers found.
   */
  public List<TransfersEntity> getAccountTransfers(Integer accountNumber) {
    log.traceEntry("getAccountTransferHistoric(accountNumber={})", accountNumber);

    log.info("Calling transferRepository to get account transfers");
    return log.traceExit(
        "getAccountTransferHistoric(accountNumber): {}",
        transferRepository.findByAccountAccountNumberOrderByDateDesc(accountNumber));
  }

  /**
   * Method responsible to operate the transfer between accounts.
   *
   * @param transferRequest The request object containing the transfer info.
   * @return The actual account balance from the origin account.
   */
  public synchronized BigDecimal transfer(TransferRequest transferRequest) {
    log.traceEntry("transfer(transferRequest={})", transferRequest);

    return log.traceExit(
        "transfer(transferRequest): {}",
        clientAccountRepository
            .findByAccountNumber(transferRequest.getOriginAccount())
            .map(
                clientAccount -> {
                  if (isValid(clientAccount, transferRequest)) {
                    transferBalances(clientAccount, transferRequest);
                  }
                  return log.traceExit("transfer(transferRequest): {}", clientAccount.getBalance());
                })
            .orElseThrow(
                () -> {
                  log.error(
                      "Could not find the origin account: {}", transferRequest.getOriginAccount());
                  return log.traceExit(
                      "transfer(transferRequest): {}",
                      new NotFoundException(
                          NotFoundErrorCodes.ORIGIN_ACCOUNT_NOT_FOUND,
                          String.format(
                              "Not found account number: %s", transferRequest.getOriginAccount())));
                }));
  }

  /**
   * Method responsible to update the balances with the transfer operation.
   *
   * @param originAccount The origin account.
   * @param transferRequest The request object containing the transfer info.
   */
  @Transactional
  private synchronized void transferBalances(
      ClientAccountEntity originAccount, TransferRequest transferRequest) {
    log.traceEntry(
        "transferBalances(originAccount={}, transferRequest={})", originAccount, transferRequest);

    log.info("Calling clientAccountRepository to get destination account");
    ClientAccountEntity destinationAccount =
        clientAccountRepository
            .findByAccountNumber(transferRequest.getDestinationAccount())
            .orElseThrow(
                () -> {
                  log.error(
                      "Could not find the destination account: {}",
                      transferRequest.getDestinationAccount());
                  saveHistoric(originAccount, transferRequest, TransferStatusEnum.FAILED);
                  return log.traceExit(
                      new NotFoundException(
                          NotFoundErrorCodes.DESTINATION_ACCOUNT_NOT_FOUND,
                          String.format(
                              "Not found destination account number: %s",
                              transferRequest.getDestinationAccount())));
                });

    log.info("Calling clientAccountRepository to update origin account balance");
    originAccount.setBalance(originAccount.getBalance().subtract(transferRequest.getAmount()));
    clientAccountRepository.save(originAccount);

    log.info("Calling clientAccountRepository to update destination account balance");
    destinationAccount.setBalance(destinationAccount.getBalance().add(transferRequest.getAmount()));
    clientAccountRepository.save(destinationAccount);

    saveHistoric(originAccount, transferRequest, TransferStatusEnum.SUCCESS);

    log.traceExit("transferBalances(originAccount, transferRequest): void");
  }

  /**
   * Method responsible to validate the transfer data.
   *
   * @param clientAccount The origin account.
   * @param transferRequest The request object containing the transfer info.
   * @return true if is valid.
   */
  private boolean isValid(ClientAccountEntity clientAccount, TransferRequest transferRequest) {
    log.traceEntry("isValid(clientAccount={}, transferRequest={})", clientAccount, transferRequest);

    if (transferRequest.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
      saveHistoric(clientAccount, transferRequest, TransferStatusEnum.FAILED);
      throw log.traceExit(
          "isValid(clientAccount, transferRequest): {}",
          new BadRequestException(
              BadRequestErrorsTypes.TRANSFER_AMOUNT_LIMIT_ACHIEVED,
              "the transfer amount must be a maximum of R$ 1000.00"));
    }

    if (transferRequest.getAmount().compareTo(clientAccount.getBalance()) > 0) {
      saveHistoric(clientAccount, transferRequest, TransferStatusEnum.FAILED);
      throw log.traceExit(
          "isValid(clientAccount, transferRequest): {}",
          new ConflictException(
              ConflictErrorsTypes.INSUFFICIENT_ACCOUNT_BALANCE,
              String.format(
                  "the account %s has not enough balance for the operation",
                  transferRequest.getOriginAccount())));
    }
    return log.traceExit("isValid(clientAccount, transferRequest): {}", true);
  }

  /**
   * Method responsible to save the transfer historic
   *
   * @param clientAccount The origin account.
   * @param transferRequest The request object containing the transfer info.
   * @param status the status of the operation {SUCCESS, FAILED}.
   */
  private void saveHistoric(
      ClientAccountEntity clientAccount,
      TransferRequest transferRequest,
      TransferStatusEnum status) {
    log.traceEntry(
        "saveHistoric(clientAccount={}, transferRequest={}, status={})",
        clientAccount,
        transferRequest,
        status);

    log.info("Calling transferRepository to save the transfer historic");
    transferRepository.save(
        TransfersEntity.builder()
            .status(status)
            .date(LocalDateTime.now(ZoneId.of("UTC")))
            .account(clientAccount)
            .destinationAccountNumber(transferRequest.getDestinationAccount())
            .amount(transferRequest.getAmount())
            .build());

    log.traceExit("saveHistoric(clientAccount, transferRequest, status): void");
  }
}
