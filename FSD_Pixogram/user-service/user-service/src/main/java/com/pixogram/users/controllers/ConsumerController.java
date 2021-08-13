package com.pixogram.users.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.users.payload.response.ProducerMediaDTO;
import com.pixogram.users.security.services.UserDetailsServiceImpl;

@RequestMapping("/consumer")
@RestController
public class ConsumerController {

	private UserDetailsServiceImpl service;

	@Autowired
	public ConsumerController(UserDetailsServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/getMessage")
	public String getMessage() {
		return service.getMessageFromProducer();
	}

	@PostMapping("/singleFileUpload")
	public String singleMediaUpload(String singleMediaRequest, MultipartFile file) {
		return service.singleFileUpload(singleMediaRequest, file);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/allMediaByUserId/{userId}")
	List<ProducerMediaDTO> allMediaByUserId(@PathVariable("userId") Long userId) {
		return service.allMediaByUserId(userId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/userfiles/{userId}")
	List<ProducerMediaDTO> getUserListFiles(@PathVariable("userId") String userId) {
		return service.getUserListFiles(userId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/files/{userId}/{filename:.+}")
	Resource getUserFile(@PathVariable String userId, @PathVariable String filename) {
		return service.getUserFile(userId, filename);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/files/{filename:.+}")
	Resource getFile(@PathVariable String filename) {
		return service.getFile(filename);
	}
	/*
	 * @PostMapping("/multipleFileUpload") public String
	 * multipleFileUpload(@ModelAttribute MultipleMediaRequest multipleMediaRequest)
	 * { return service.multipleFileUpload(multipleMediaRequest); }
	 * 
	 */
}
