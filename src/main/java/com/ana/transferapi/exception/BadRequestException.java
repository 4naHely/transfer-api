/*
 * Bad Request exception class.
 */
package com.ana.transferapi.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = false)
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

  private final BadRequestErrorsTypes type;
  private final String friendlyMessage;

  public enum BadRequestErrorsTypes {
    TRANSFER_AMOUNT_LIMIT_ACHIEVED
  }

  /**
   * Class constructor specifying the error type, friendly message.
   *
   * @param type The error type.
   * @param friendlyMessage The friendly message.
   */
  public BadRequestException(BadRequestErrorsTypes type, String friendlyMessage) {
    super(friendlyMessage);
    this.type = type;
    this.friendlyMessage = friendlyMessage;
  }
}
