package com.pixogram.users.security.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.users.models.User;
import com.pixogram.users.payload.request.MultipleMediaRequest;
import com.pixogram.users.payload.response.ProducerMediaDTO;
import com.pixogram.users.repository.UserRepository;
import com.pixogram.users.security.services.client.ProducerClient;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserDetailsImpl.build(user);
	}

	private ProducerClient producerClient;

	@Autowired
	public void setProducerClient(ProducerClient producerClient) {
		this.producerClient = producerClient;
	}

	public List<ProducerMediaDTO> allMediaByUserId(Long userId) {
		return producerClient.allMediaByUserId(userId);
	}

	public String getMessageFromProducer() {
		return producerClient.getMessage();
	}

	public String singleFileUpload(String singleMediaRequest, MultipartFile file) {
		return producerClient.singleFileUpload(singleMediaRequest, file);
	}

	public List<ProducerMediaDTO> getUserListFiles(String userId) {
		return producerClient.getUserListFiles(userId);
	}

	public Resource getFile(String filename) {
		return producerClient.getFile(filename);
	}

	public Resource getUserFile(String userId, String filename) {
		return producerClient.getUserFile(userId, filename);
	}

	public String multipleFileUpload(MultipleMediaRequest multipleMediaRequest) {
		return producerClient.multipleFileUpload(multipleMediaRequest);
	}

}
