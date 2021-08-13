package com.pixogram.media.service;

import java.io.IOException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.media.dto.MediaDTO;
import com.pixogram.media.dto.MultipleMediaRequest;
import com.pixogram.media.dto.SingleMediaRequest;
import com.pixogram.media.entity.Media;
import com.pixogram.media.exception.FileStorageException;
import com.pixogram.media.exception.MyFileNotFoundException;
import com.pixogram.media.repository.MediaRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Anushkha Thakur
 *
 */
@Service
@Slf4j
public class MediaServiceImpl implements MediaService {

	private final Path root = Paths.get("uploads");
	private Path fileStorageLocation = null;

	@Autowired
	private MediaRepository mediaRepository;

	@Override
	public void init() {
		try {
			Files.createDirectory(root);
			log.info("Init : Creating root directory");
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	/***
	 * Method to create the directory
	 */
	@Override
	public String save(MultipartFile file, long userId) {
		String fileName = "pixogram" + Math.random() + file.getOriginalFilename();
		try {
			Path userIdPath = Paths.get(root + "/" + String.valueOf(userId));
			log.info("Creating directory for user");
			Files.createDirectories(userIdPath);
			Files.copy(file.getInputStream(), userIdPath.resolve(fileName.trim()));
			log.info("user file copy done");
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
		return fileName.trim();
	}

	/***
	 * Method to load the file with file name
	 */
	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			log.info("Executing method to load files with file name");
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	/***
	 * Method to load user files with file name
	 */
	@Override
	public Resource loadUserFile(String filename, String userId) {
		try {
			Path file = Paths.get("uploads/" + userId).resolve(filename);
			log.info("loadUserFile:file" + file);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	/***
	 * Method to check file exist or not
	 */
	@Override
	public boolean fileExist(String filename, String userId) throws MalformedURLException {
		Path file = Paths.get("uploads/" + userId).resolve(filename);
		Resource resource = new UrlResource(file.toUri());
		if (resource.exists() || resource.isReadable()) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * Method to delete all static directory on restart of micro service
	 */
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	/***
	 * Method to load all the files path
	 */
	@Override
	public Stream<Path> loadAll() {
		try {
			log.info("Executing loadAll");
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	/***
	 * Method to load user files path according to user id
	 */
	@Override
	public Stream<Path> loadUserFiles(String userId) {
		try {
			Path userFilePath = Paths.get("uploads/" + userId);
			log.info("Executing loadUserFiles: with userId:" + userId);
			return Files.walk(userFilePath, 1).filter(path -> !path.equals(userFilePath)).map(userFilePath::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	/***
	 * Method to upload single file
	 */
	@Override
	public Media uploadFile(SingleMediaRequest media, MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Media mediaEntity = new Media();
		mediaEntity.setMimeType(file.getContentType());
		mediaEntity.setMediaCaption(media.getDesc());
		mediaEntity.setMediaTitle(media.getMediaTitle());
		mediaEntity.setDate(new Date());
		mediaEntity.setUserId(media.getUserId());
		log.info("Executing uploadFile with file :" + mediaEntity.getName());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			log.info("creating directories...with file name :" + fileName);
			fileName = save(file, media.getUserId());
			mediaEntity.setName(fileName);
			Path mediaUrl = Paths.get("uploads" + "/" + media.getUserId().toString());
			mediaEntity.setMediaUrl(mediaUrl.resolve(fileName).toString());
			mediaEntity = mediaRepository.save(mediaEntity);
			log.info("File details saved in tables");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
		return mediaEntity;
	}

	/***
	 * Method to get all files
	 */
	@Override
	public List<Media> getAllFiles() {
		return mediaRepository.findAll();
	}

	/***
	 * Method to load file as resource
	 */
	@Override
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

	/***
	 * Method to fetch media by user id
	 */
	@Override
	public List<Media> findMediaByUserId(Long userId) {
		log.info("Executing findByUserId of MediaService");
		return mediaRepository.findMediaByUserId(userId);
	}

	/***
	 * Method to upload multiple files
	 */
	@Override
	public String uploadMutipleFile(MultipleMediaRequest mediaRequest) throws IOException {
		MultipartFile file;
		List<MediaDTO> mediaList = mediaRequest.getMediaList();
		for (MediaDTO media : mediaList) {
			file = media.getFile();
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Media mediaEntity = new Media();
			mediaEntity.setMimeType(file.getContentType());
			mediaEntity.setMediaCaption(media.getDesc());
			mediaEntity.setMediaTitle(media.getMediaTitle());
			mediaEntity.setDate(new Date());
			mediaEntity.setUserId(media.getUserId());
			log.info("Executing uploadMutipleFile method : " + mediaEntity.getName());

			try {
				if (fileName.contains("..")) {
					throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
				}
				log.info("creating directories...with fileName:" + fileName);
				fileName = save(file, media.getUserId());
				mediaEntity.setName(fileName);
				Path mediaUrl = Paths.get("uploads" + "/" + media.getUserId().toString());
				mediaEntity.setMediaUrl(mediaUrl.resolve(fileName).toString());
				mediaEntity = mediaRepository.save(mediaEntity);
				log.info("file details saved in db");
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
			}
		}

		return "Multiple files uploaded successfully";
	}

}
