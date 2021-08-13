package com.pixogram.users.security.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.users.models.User;
import com.pixogram.users.payload.response.ProducerMediaDTO;
import com.pixogram.users.payload.response.ResponseMessage;
import com.pixogram.users.repository.UserRepository;
import com.pixogram.users.security.services.client.ProducerClient;

/**
 * @author Anushkha Thakur
 *
 */
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

	public String getMessageFromProducer() {
		return producerClient.getMessageFromProducer();
	}

	public ResponseEntity<ResponseMessage> singleFileUpload(String singleMediaRequest, MultipartFile file) {
		return producerClient.singleFileUpload(singleMediaRequest, file);
	}
	 

	public ResponseEntity<List<ProducerMediaDTO>> getUserListFiles(String userId) {
		return producerClient.getUserListFiles(userId);
	}

}
