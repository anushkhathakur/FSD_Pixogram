package com.pixogram.users.security.services.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.users.payload.request.MultipleMediaRequest;
import com.pixogram.users.payload.response.ProducerMediaDTO;

@FeignClient(name = "spring-feign-producer")
public interface ProducerClient {

	@RequestMapping(method = RequestMethod.GET, value = "/producer")
	String getMessage();

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/allMediaByUserId/{userId}")
	List<ProducerMediaDTO> allMediaByUserId(@PathVariable("userId") Long userId);

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/userfiles/{userId}")
	List<ProducerMediaDTO> getUserListFiles(@PathVariable("userId") String userId);

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/files/{userId}/{filename:.+}")
	Resource getUserFile(@PathVariable String userId, @PathVariable String filename);

	@RequestMapping(method = RequestMethod.GET, value = "/producer/media/files/{filename:.+}")
	Resource getFile(@PathVariable String filename);

	// Note : It was not possible to send multipart file in SingleMediaRequest
	// object that is the reason @RequestPart is used
	@RequestMapping(method = RequestMethod.POST, value = "/producer/media/singleFileUpload")
	String singleFileUpload(@RequestPart("singleMediaRequest") String singleMediaRequest,
			@RequestPart("file") MultipartFile file);

	@RequestMapping(method = RequestMethod.POST, value = "/producer/media/multipleFileUpload")
	String multipleFileUpload(@ModelAttribute MultipleMediaRequest multipleMediaRequest);

}
