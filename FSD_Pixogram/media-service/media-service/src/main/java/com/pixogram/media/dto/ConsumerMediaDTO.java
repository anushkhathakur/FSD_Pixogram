/**
 * 
 */
package com.pixogram.media.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 000A6R744
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerMediaDTO {
	private Long mediaId;
	private Long userId;
	private String mediaUrl;
	private String mimeType;
	private String mediaTitle;
	private String mediaCaption;
	private Date uploadedDateTime;
	private boolean hide;
	private String name;
}
