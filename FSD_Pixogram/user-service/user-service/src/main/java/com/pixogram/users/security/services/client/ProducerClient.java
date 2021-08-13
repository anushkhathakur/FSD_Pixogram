package com.pixogram.users.security.services.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.users.payload.response.ProducerMediaDTO;
import com.pixogram.users.payload.response.ResponseMessage;

/**
 * @author Anushkha Thakur
 *
 */
@FeignClient(name = "spring-feign-producer")
public interface ProducerClient {

	/***
	 * Rest Api to test the message
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/producer/media")
	String getMessageFromProducer();

	/***
	 * Rest Api to fetch all file details for user
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/userfiles/{userId}")
	ResponseEntity<List<ProducerMediaDTO>> getUserListFiles(@PathVariable("userId") String userId);


	/**
	 * Rest API to upload single file and details, @RequestPart attribute is used as
	 * it was not possible to upload mutipart file from angular
	 * 
	 * @param singleMediaRequest
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/producer/media/singleFileUpload", method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	ResponseEntity<ResponseMessage> singleFileUpload(@RequestPart("singleMediaRequest") String singleMediaRequest,

			@RequestPart("file") MultipartFile file);

}
