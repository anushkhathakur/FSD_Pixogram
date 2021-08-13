package com.pixogram.media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Anushkha Thakur
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFile {
  private String name;
  private String url;
  private String type;
  private long size;

}
