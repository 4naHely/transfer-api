/*
 * Represents Database Table client_account
 */
package com.ana.transferapi.repository.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "client_account")
public class ClientAccountEntity implements Serializable {
  @Id
  @Column(name = "client_id", unique = true, nullable = false)
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(name = "account_number", columnDefinition = "serial", unique = true)
  @Generated(GenerationTime.INSERT)
  private Integer accountNumber;

  @Column private BigDecimal balance;
}
