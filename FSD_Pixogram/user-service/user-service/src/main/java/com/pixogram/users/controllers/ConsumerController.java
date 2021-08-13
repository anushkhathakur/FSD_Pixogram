package com.pixogram.users.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.users.payload.response.ProducerMediaDTO;
import com.pixogram.users.payload.response.ResponseMessage;
import com.pixogram.users.security.services.UserDetailsServiceImpl;

/**
 * @author Anushkha Thakur
 *
 */
@RequestMapping("/consumer")
@RestController
public class ConsumerController {

	private UserDetailsServiceImpl service;

	@Autowired
	public ConsumerController(UserDetailsServiceImpl service) {
		this.service = service;
	}

	/***
	 * Rest api of media-service producer to get message
	 * 
	 * @return
	 */
	@GetMapping("/getMessage")
	public String getMessage() {
		return service.getMessageFromProducer();
	}

	/***
	 * Rest api of media-service producer to file upload
	 * 
	 * @return
	 */
	@RequestMapping(value = "/producer/media/singleFileUpload", method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	public ResponseEntity<ResponseMessage> singleMediaUpload(String singleMediaRequest, MultipartFile file) {
		return service.singleFileUpload(singleMediaRequest, file);
	}

	/***
	 * Rest api of media-service producer to get all user files
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/userfiles/{userId}")
	ResponseEntity<List<ProducerMediaDTO>> getUserListFiles(@PathVariable("userId") String userId) {
		return service.getUserListFiles(userId);
	}

}
