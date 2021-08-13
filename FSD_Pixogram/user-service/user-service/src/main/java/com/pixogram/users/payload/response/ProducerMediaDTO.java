package com.pixogram.users.payload.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerMediaDTO {
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
