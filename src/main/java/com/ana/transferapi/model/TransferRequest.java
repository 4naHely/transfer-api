/*
 * Transfer request class.
 */
package com.ana.transferapi.model;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TransferRequest {
  @NotNull(message = "Origin account must not be blank")
  private Integer originAccount;

  @NotNull(message = "Destination account must not be blank")
  private Integer destinationAccount;

  @NotNull(message = "Amount must not be null")
  private BigDecimal amount;
}
