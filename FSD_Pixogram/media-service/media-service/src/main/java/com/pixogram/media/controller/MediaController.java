package com.pixogram.media.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

@RestController
@RequestMapping("/producer/media")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class MediaController {
	private final Path root = Paths.get("uploads");
	@Autowired
	private MediaService mediaService;

	@GetMapping
	public String healthCheck() {
		return "Feign Producer is working fine ";
	}

	@RequestMapping(value = "/singleFileUpload", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<ResponseMessage> uploadSingleFile(
			@RequestPart("singleMediaRequest") String singleMediaRequest, @RequestPart("file") MultipartFile file) {
		try {
			SingleMediaRequest singleMediaRequestEntity = new ObjectMapper().readValue(singleMediaRequest,
					SingleMediaRequest.class);

			log.info("inside upload api -uploadFile method ");
			System.out.println("file.getOriginalFilename():" + file.getOriginalFilename());
			mediaService.uploadFile(singleMediaRequestEntity, file);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
		}
	}

	@PostMapping(value = "/multipleFileUpload")
	public ResponseEntity<ResponseMessage> uploadMultipleFile(
			@ModelAttribute MultipleMediaRequest multipleMediaRequest) {
		System.out.println("media-service : uploadMultipleFile method called..");

		log.info("Inside uploadMultipleFile method of MediaController.....");
		// MultipleMediaRequest singleMediaRequestEntity = new
		// ObjectMapper().readValue(multipleMediaRequest, MultipleMediaRequest.class);
		try {
			for (SingleMediaRequest mediaDTO : multipleMediaRequest.getMediaList()) {
				// String fileName = mediaDTO.getFile().getOriginalFilename();
				// System.out.println("fileName:" + fileName);
				// mediaService.uploadFile(mediaDTO);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage("Uploaded multiple files successfully"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseMessage("Could not upload the file: !"));
		}
	}

	@GetMapping(value = "/allMediaByUserId/{userId}")
	public ResponseEntity<?> findMediaByUserId(@PathVariable("userId") Long userId) {
		log.info("Inside findMediaById method of MediaController");
		List<ConsumerMediaDTO> consumerMediaDTOList = new ArrayList<ConsumerMediaDTO>();
		List<Media> mediaList = mediaService.findMediaByUserId(userId);
		for (Media media : mediaList) {
			ConsumerMediaDTO consumerMediaDTO = new ConsumerMediaDTO();
			consumerMediaDTO.setHide(media.isHide());
			consumerMediaDTO.setMediaCaption(media.getMediaCaption());
			consumerMediaDTO.setMediaId(media.getMediaId());
			consumerMediaDTO.setMediaTitle(media.getMediaTitle());
			consumerMediaDTO.setMediaUrl(media.getMediaUrl());
			consumerMediaDTO.setMimeType(media.getMimeType());
			consumerMediaDTO.setName(media.getName());
			consumerMediaDTO.setUploadedDateTime(media.getDate());
			consumerMediaDTO.setUserId(media.getUserId());
			consumerMediaDTOList.add(consumerMediaDTO);
		}
		return ResponseEntity.ok(consumerMediaDTOList);
	}

	@GetMapping("/files")
	public ResponseEntity<List<ConsumerMediaDTO>> getListFiles() {
		List<FileInfo> filePathInfos = mediaService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(MediaController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());
		
		System.out.println("filePathInfos==========>"+filePathInfos);
		List<Media> mediaList = mediaService.getAllFiles();
		System.out.println("mediaList.size()"+mediaList.size());
		List<ConsumerMediaDTO> fileInfos = new ArrayList<ConsumerMediaDTO>();
		for(Media media :mediaList) {
			System.out.println("getName==>"+media.getName());
			System.out.println("getMediaUrl====>"+media.getMediaUrl());
			for(FileInfo fileInfo : filePathInfos){
				System.out.println("fileInfo.getName()==>"+fileInfo.getName());
				System.out.println("fileInfo.getUrl()====>"+fileInfo.getUrl());
				if(fileInfo.getName().equalsIgnoreCase(media.getName())) {
					ConsumerMediaDTO fileDetails = new ConsumerMediaDTO();
					fileDetails.setHide(media.isHide());
					fileDetails.setMediaCaption(media.getMediaCaption());
					fileDetails.setMediaId(media.getMediaId());
					fileDetails.setMediaTitle(media.getMediaTitle());
					System.out.println("Url===>"+fileInfo.getUrl());
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
	
	@GetMapping("/userfiles/{userId}")
	public ResponseEntity<List<ConsumerMediaDTO>> getUserListFiles(@PathVariable("userId") String userId) {
		System.out.println("I am in getUserListFiles-- "+userId);
		List<FileInfo> filePathInfos = mediaService.loadUserFiles(userId).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(MediaController.class, "getUserFile",userId, path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());
		
		System.out.println("filePathInfos==========>"+filePathInfos);
		List<Media> mediaList = mediaService.findMediaByUserId(Long.valueOf(userId));
		System.out.println("mediaList.size()"+mediaList.size());
		List<ConsumerMediaDTO> fileInfos = new ArrayList<ConsumerMediaDTO>();
		for(Media media :mediaList) {
			System.out.println("getName==>"+media.getName());
			System.out.println("getMediaUrl====>"+media.getMediaUrl());
			for(FileInfo fileInfo : filePathInfos){
				System.out.println("fileInfo.getName()==>"+fileInfo.getName());
				System.out.println("fileInfo.getUrl()====>"+fileInfo.getUrl());
				if(fileInfo.getName().equalsIgnoreCase(media.getName())) {
					ConsumerMediaDTO fileDetails = new ConsumerMediaDTO();
					fileDetails.setHide(media.isHide());
					fileDetails.setMediaCaption(media.getMediaCaption());
					fileDetails.setMediaId(media.getMediaId());
					fileDetails.setMediaTitle(media.getMediaTitle());
					System.out.println("Url===>"+fileInfo.getUrl());
					fileDetails.setMediaUrl(fileInfo.getUrl());
					fileDetails.setMimeType(media.getMimeType());
					fileDetails.setName(media.getName());
					fileDetails.setUploadedDateTime(media.getDate());
					fileDetails.setUserId(media.getUserId());
					try {
						if(mediaService.fileExist(media.getName(), userId)) {
							fileInfos.add(fileDetails);
						}
					} catch (MalformedURLException e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		System.out.println("Hi I m in getFile-------------------");
		Resource file = mediaService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	@GetMapping("/files/{userId}/{filename:.+}")
	public ResponseEntity<Resource> getUserFile(@PathVariable String userId,@PathVariable String filename) {
		System.out.println("Hi I m in getUserFile-------------------");
		Resource file = mediaService.loadUserFile(filename,userId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
}
