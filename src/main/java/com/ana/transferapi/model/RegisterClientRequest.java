/*
 * Register client request class.
 */
package com.ana.transferapi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterClientRequest implements Serializable {

  @NotBlank(message = "Name must not be blank")
  private String name;

  @NotNull(message = "Balance must not be blank")
  private BigDecimal balance;
}
