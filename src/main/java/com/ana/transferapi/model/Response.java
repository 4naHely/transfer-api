/*
 * Response model class.
 */
package com.ana.transferapi.model;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {
  @Builder.Default private Instant timestamp = Instant.now();
  private String path;
  private String message;
  private Object body;
  private String statusCode;
}
