/*
 * Represents Database Table transfer_historic
 */
package com.ana.transferapi.repository.entity;

import com.ana.transferapi.enums.TransferStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "transfers")
public class TransfersEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private TransferStatusEnum status;

  @Column(nullable = false)
  private LocalDateTime date;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private Integer destinationAccountNumber;

  @ManyToOne()
  @JoinColumn(name = "client_id", nullable = false)
  private ClientAccountEntity account;
}
