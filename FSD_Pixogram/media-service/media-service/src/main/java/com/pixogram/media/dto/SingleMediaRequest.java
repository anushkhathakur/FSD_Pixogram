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
public class SingleMediaRequest {
    private Long userId;
    private String mediaTitle;
    private String desc;
    private String [] tags;
    private String [] effect;
    private String fileName;
}
