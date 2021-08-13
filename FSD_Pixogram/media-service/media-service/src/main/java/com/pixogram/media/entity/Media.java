package com.pixogram.media.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Anushkha Thakur
 *
 */
@Entity
@Table(name = "media")
public class Media {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;
    private Long userId;
    private String mediaUrl;
    private String mimeType;
    private String mediaCaption;
    private String mediaTitle;
    private Date date;
    private boolean hide;
    private String name;
    
    public Media() {

	}
	/**
	 * @return the mediaId
	 */
	public Long getMediaId() {
		return mediaId;
	}
	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl() {
		return mediaUrl;
	}
	/**
	 * @param mediaUrl the mediaUrl to set
	 */
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}
	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	/**
	 * @return the mediaTitle
	 */
	public String getMediaTitle() {
		return mediaTitle;
	}
	/**
	 * @param mediaTitle the mediaTitle to set
	 */
	public void setMediaTitle(String mediaTitle) {
		this.mediaTitle = mediaTitle;
	}
	/**
	 * @return the mediaCaption
	 */
	public String getMediaCaption() {
		return mediaCaption;
	}
	/**
	 * @param mediaCaption the mediaCaption to set
	 */
	public void setMediaCaption(String mediaCaption) {
		this.mediaCaption = mediaCaption;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the hide
	 */
	public boolean isHide() {
		return hide;
	}
	/**
	 * @param hide the hide to set
	 */
	public void setHide(boolean hide) {
		this.hide = hide;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
    
}
