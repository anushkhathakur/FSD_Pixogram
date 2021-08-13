package com.pixogram.media.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pixogram.media.dto.SingleMediaRequest;
import com.pixogram.media.entity.Media;

@Service
public interface MediaService {
	public void init();
	public String save(MultipartFile file,long userId);
	public Resource load(String filename);
	public void deleteAll();
	public Stream<Path> loadAll();
	public Media uploadFile(SingleMediaRequest media, MultipartFile file) throws IOException;
	public List<Media> getAllFiles();
	public Resource loadFileAsResource(String fileName);
	public List<Media> findMediaByUserId(Long userId);
	public Stream<Path> loadUserFiles(String userId);
	public Resource loadUserFile(String filename,String userId);
	public boolean fileExist(String filename, String userId) throws MalformedURLException;
}
