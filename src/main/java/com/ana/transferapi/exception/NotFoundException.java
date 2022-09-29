/*
 * Not found exception class.
 */
package com.ana.transferapi.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = false)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

  private final NotFoundErrorCodes type;
  private final String friendlyMessage;

  public enum NotFoundErrorCodes {
    ORIGIN_ACCOUNT_NOT_FOUND,
    DESTINATION_ACCOUNT_NOT_FOUND,
    ACCOUNT_NOT_FOUND
  }

  /**
   * Class constructor specifying a String error type and a friendly message.
   *
   * @param type The error type.
   * @param friendlyMessage The friendly message.
   */
  public NotFoundException(NotFoundErrorCodes type, String friendlyMessage) {
    super(friendlyMessage);
    this.type = type;
    this.friendlyMessage = friendlyMessage;
  }
}
