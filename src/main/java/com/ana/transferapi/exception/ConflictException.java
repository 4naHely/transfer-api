/*
 * Conflict exception class.
 */
package com.ana.transferapi.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = false)
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

  private final ConflictErrorsTypes type;
  private final String friendlyMessage;

  public enum ConflictErrorsTypes {
    INSUFFICIENT_ACCOUNT_BALANCE
  }

  /**
   * Class constructor specifying the error type, friendly message.
   *
   * @param type The error type.
   * @param friendlyMessage The friendly message.
   */
  public ConflictException(ConflictErrorsTypes type, String friendlyMessage) {
    super(friendlyMessage);
    this.type = type;
    this.friendlyMessage = friendlyMessage;
  }
}
