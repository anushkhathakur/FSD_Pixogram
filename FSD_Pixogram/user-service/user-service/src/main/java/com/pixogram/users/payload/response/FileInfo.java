package com.pixogram.users.payload.response;

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
public class FileInfo {
  private String name;
  private String url;
    
}
