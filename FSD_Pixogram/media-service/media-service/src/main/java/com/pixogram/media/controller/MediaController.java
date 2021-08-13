package com.pixogram.media.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixogram.media.dto.ConsumerMediaDTO;
import com.pixogram.media.dto.FileInfo;
import com.pixogram.media.dto.MultipleMediaRequest;
import com.pixogram.media.dto.ResponseMessage;
import com.pixogram.media.dto.SingleMediaRequest;
import com.pixogram.media.entity.Media;
import com.pixogram.media.service.MediaService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Anushkha Thakur
 *
 */
@RestController
@RequestMapping("/producer/media")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class MediaController {
	@Autowired
	private MediaService mediaService;

	@GetMapping
	public String healthCheck() {
		return "Feign Producer is working fine ";
	}

	/***
	 * Rest Api to retrieve all files
	 * 
	 * @return
	 */
	@GetMapping("/files")
	public ResponseEntity<List<ConsumerMediaDTO>> getListFiles() {
		log.info("files api executing..");
		List<FileInfo> filePathInfos = mediaService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(MediaController.class, "getFile", path.getFileName().toString()).build().toString();
			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		List<Media> mediaList = mediaService.getAllFiles();
		List<ConsumerMediaDTO> fileInfos = new ArrayList<ConsumerMediaDTO>();
		for (Media media : mediaList) {
			for (FileInfo fileInfo : filePathInfos) {
				if (fileInfo.getName().equalsIgnoreCase(media.getName())) {
					ConsumerMediaDTO fileDetails = new ConsumerMediaDTO();
					fileDetails.setHide(media.isHide());
					fileDetails.setMediaCaption(media.getMediaCaption());
					fileDetails.setMediaId(media.getMediaId());
					fileDetails.setMediaTitle(media.getMediaTitle());
					fileDetails.setMediaUrl(fileInfo.getUrl());
					fileDetails.setMimeType(media.getMimeType());
					fileDetails.setName(media.getName());
					fileDetails.setUploadedDateTime(media.getDate());
					fileDetails.setUserId(media.getUserId());
					fileInfos.add(fileDetails);
				}
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	/***
	 * Rest Api to fetch all file details for user
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/userfiles/{userId}")
	public ResponseEntity<List<ConsumerMediaDTO>> getUserListFiles(@PathVariable("userId") String userId) {
		log.info("userfiles api executing with user Id.." + userId);
		System.out.println("I am in getUserListFiles-- " + userId);
		List<FileInfo> filePathInfos = mediaService.loadUserFiles(userId).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(MediaController.class, "getUserFile", userId, path.getFileName().toString()).build()
					.toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		List<Media> mediaList = mediaService.findMediaByUserId(Long.valueOf(userId));
		List<ConsumerMediaDTO> fileInfos = new ArrayList<ConsumerMediaDTO>();
		for (Media media : mediaList) {
			for (FileInfo fileInfo : filePathInfos) {
				if (fileInfo.getName().equalsIgnoreCase(media.getName())) {
					ConsumerMediaDTO fileDetails = new ConsumerMediaDTO();
					fileDetails.setHide(media.isHide());
					fileDetails.setMediaCaption(media.getMediaCaption());
					fileDetails.setMediaId(media.getMediaId());
					fileDetails.setMediaTitle(media.getMediaTitle());
					fileDetails.setMediaUrl(fileInfo.getUrl());
					fileDetails.setMimeType(media.getMimeType());
					fileDetails.setName(media.getName());
					fileDetails.setUploadedDateTime(media.getDate());
					fileDetails.setUserId(media.getUserId());
					try {
						if (mediaService.fileExist(media.getName(), userId)) {
							fileInfos.add(fileDetails);
						}
					} catch (MalformedURLException e) {
						log.error(e.getMessage());
						return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(fileInfos);
					}
				}
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	/***
	 * Rest API to fetch file with particular file name
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		log.info("files api executing with filename.." + filename);
		Resource file = mediaService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	/***
	 * Rest Api to fetch file with user id and file name
	 * 
	 * @param userId
	 * @param filename
	 * @return
	 */
	@GetMapping("/files/{userId}/{filename:.+}")
	public ResponseEntity<Resource> getUserFile(@PathVariable String userId, @PathVariable String filename) {
		log.info("files api executing with filename.." + filename + " and userId .." + userId);
		Resource file = mediaService.loadUserFile(filename, userId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	/**
	 * Rest API to upload single file and details, @RequestPart attribute is used as
	 * it was not possible to upload mutipart file from angular
	 * 
	 * @param singleMediaRequest
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/singleFileUpload", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<ResponseMessage> uploadSingleFile(
			@RequestPart("singleMediaRequest") String singleMediaRequest, @RequestPart("file") MultipartFile file) {
		try {
			SingleMediaRequest singleMediaRequestEntity = new ObjectMapper().readValue(singleMediaRequest,
					SingleMediaRequest.class);

			log.info("singleFileUpload api executing..");
			mediaService.uploadFile(singleMediaRequestEntity, file);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
		}
	}

	/***
	 * Rest API to upload mutiple file with MultipleMediaRequest object
	 * 
	 * @param mediaRequest
	 * @return
	 */
	@PostMapping(value = "/multipleFileUpload")
	public ResponseEntity<ResponseMessage> multipleFileUpload(
			@ModelAttribute MultipleMediaRequest mediaRequest) {
		String message = "Uploaded multiple files successfully";
		log.info("Inside uploadMultipleFile method of MediaController.....");
		System.out.println("Inside uploadMultipleFile method of MediaController.....");
		try {
			message = mediaService.uploadMutipleFile(mediaRequest);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseMessage("Could not upload the file: !"));
		}
	}

}
